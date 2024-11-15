/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.utils.ERROR_LOADING_POKEMON_LIST
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import com.github.orioonyx.pokedex.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchPokemonListUseCase: FetchPokemonListUseCase
) : ViewModel() {

    private val _pokemonList = MutableLiveData<List<Pokemon>>(emptyList())
    val pokemonList: LiveData<List<Pokemon>> get() = _pokemonList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>> get() = _toastMessage

    private val _isFetchFailed = MutableLiveData(false)
    val isFetchFailed: LiveData<Boolean> get() = _isFetchFailed

    private var currentPageIndex = 0

    init {
        Timber.d("MainViewModel initialized")
    }

    fun fetchNextPokemonList() {
        if (_isLoading.value == true) return
        currentPageIndex++
        fetchPokemonList(currentPageIndex)
    }

    private fun fetchPokemonList(page: Int) {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            fetchPokemonListUseCase(page)
                .onStart { handleLoadingStart() }
                .catch { handleError(it) }
                .collect { handleSuccess(it) }
            handleLoadingComplete()
            EspressoIdlingResource.decrement()
        }
    }

    private fun handleLoadingStart() {
        _isLoading.value = true
        _isFetchFailed.value = false
    }

    private fun handleError(exception: Throwable) {
        Timber.e(exception, ERROR_LOADING_POKEMON_LIST)
        _toastMessage.value = Event(ERROR_LOADING_POKEMON_LIST)
        _isFetchFailed.value = true
    }

    private fun handleSuccess(list: List<Pokemon>) {
        _pokemonList.value = _pokemonList.value.orEmpty().plus(list)
    }

    private fun handleLoadingComplete() {
        _isLoading.value = false
        if (_pokemonList.value.isNullOrEmpty()) {
            _isFetchFailed.value = true
            _toastMessage.value = Event(ERROR_LOADING_POKEMON_LIST)
        }
    }
}
