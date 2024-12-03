/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.utils.ERROR_LOADING_POKEMON_LIST
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import com.github.orioonyx.pokedex.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchPokemonListUseCase: FetchPokemonListUseCase
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _toastMessage = MutableStateFlow<Event<String>?>(null)
    val toastMessage: StateFlow<Event<String>?> = _toastMessage

    private val _isFetchFailed = MutableStateFlow(false)
    val isFetchFailed: StateFlow<Boolean> = _isFetchFailed

    private var currentPageIndex = 0

    init {
        Timber.d("MainViewModel initialized")
    }

    fun fetchNextPokemonList() {
        if (_isLoading.value) return
        currentPageIndex++
        fetchPokemonList(currentPageIndex)
    }

    private fun fetchPokemonList(page: Int) = viewModelScope.launch {
        withIdlingResource {
            fetchPokemonListUseCase(page)
                .handleFetchResult()
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
        _isFetchFailed.value = !isLoading && _pokemonList.value.isEmpty()
    }

    private fun updatePokemonList(newList: List<Pokemon>) {
        _pokemonList.value += newList
    }

    private fun handleFetchError(exception: Throwable) {
        Timber.e(exception, ERROR_LOADING_POKEMON_LIST)
        _toastMessage.value = Event(ERROR_LOADING_POKEMON_LIST)
        _isFetchFailed.value = true
    }

    private suspend fun Flow<List<Pokemon>>.handleFetchResult() {
        this.onStart { updateLoadingState(true) }
            .catch { handleFetchError(it) }
            .collect { updatePokemonList(it) }
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
}
