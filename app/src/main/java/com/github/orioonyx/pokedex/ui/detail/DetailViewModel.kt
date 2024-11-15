/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
import com.github.orioonyx.pokedex.utils.ERROR_LOADING_POKEMON_DETAILS
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import com.github.orioonyx.pokedex.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val fetchPokemonDetailUseCase: FetchPokemonDetailUseCase
) : ViewModel() {

    private val _pokemonDetail = MutableLiveData<PokemonDetail?>()
    val pokemonDetail: LiveData<PokemonDetail?> get() = _pokemonDetail

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>> get() = _toastMessage

    private val _isFetchFailed = MutableLiveData(false)
    val isFetchFailed: LiveData<Boolean> get() = _isFetchFailed

    init {
        Timber.d("DetailViewModel initialized")
    }

    fun fetchPokemonDetail(name: String) {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            try {
                fetchPokemonDetailUseCase(name)
                    .onStart { onFetchStart() }
                    .catch { onFetchError(it) }
                    .collect { detail -> onFetchSuccess(detail) }
            } finally {
                onFetchComplete()
                EspressoIdlingResource.decrement()
            }
        }
    }

    private fun onFetchStart() {
        _isLoading.value = true
        _isFetchFailed.value = false
    }

    private fun onFetchError(exception: Throwable) {
        Timber.e(exception, ERROR_LOADING_POKEMON_DETAILS)
        _toastMessage.value = Event(ERROR_LOADING_POKEMON_DETAILS)
        _isFetchFailed.value = true
    }

    private fun onFetchSuccess(detail: PokemonDetail?) {
        _pokemonDetail.value = detail
    }

    private fun onFetchComplete() {
        _isLoading.value = false
        if (_pokemonDetail.value?.name.isNullOrEmpty()) {
            _isFetchFailed.value = true
            _toastMessage.value = Event(ERROR_LOADING_POKEMON_DETAILS)
        }
    }

    fun setToastMessage(message: String) {
        _toastMessage.value = Event(message)
    }
}