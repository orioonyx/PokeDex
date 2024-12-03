/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
import com.github.orioonyx.pokedex.utils.ERROR_LOADING_POKEMON_DETAILS
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import com.github.orioonyx.pokedex.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val fetchPokemonDetailUseCase: FetchPokemonDetailUseCase
) : ViewModel() {

    private val _pokemonDetail = MutableStateFlow(PokemonDetail.default())
    val pokemonDetail: StateFlow<PokemonDetail> = _pokemonDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _toastMessage = MutableStateFlow<Event<String>?>(null)
    val toastMessage: StateFlow<Event<String>?> = _toastMessage.asStateFlow()

    private val _isFetchFailed = MutableStateFlow(false)
    val isFetchFailed: StateFlow<Boolean> = _isFetchFailed.asStateFlow()

    init {
        Timber.d("DetailViewModel initialized")
    }

    fun fetchPokemonDetail(name: String) = viewModelScope.launch {
        withIdlingResource {
            fetchPokemonDetailUseCase(name)
                .handleFetchResult()
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
        _isFetchFailed.value = !isLoading && _pokemonDetail.value.name.isEmpty()
    }

    private fun updatePokemonDetail(detail: PokemonDetail) {
        _pokemonDetail.value = detail
    }

    private fun handleFetchError(exception: Throwable) {
        Timber.e(exception, ERROR_LOADING_POKEMON_DETAILS)
        _toastMessage.value = Event(ERROR_LOADING_POKEMON_DETAILS)
        _isFetchFailed.value = true
    }

    private suspend fun Flow<PokemonDetail>.handleFetchResult() {
        this.onStart { updateLoadingState(true) }
            .catch { handleFetchError(it) }
            .collect { updatePokemonDetail(it) }
        updateLoadingState(false)
    }

    private inline fun withIdlingResource(block: () -> Unit) {
        EspressoIdlingResource.increment()
        try {
            block()
        } finally {
            EspressoIdlingResource.decrement()
        }
    }

    fun setToastMessage(message: String) {
        _toastMessage.value = Event(message)
    }
}