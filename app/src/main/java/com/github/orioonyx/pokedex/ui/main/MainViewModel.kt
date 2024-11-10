package com.github.orioonyx.pokedex.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.utils.EspressoIdlingResource
import com.github.orioonyx.pokedex.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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

    private var pokemonFetchingIndex = 0

    init {
        Timber.d("MainViewModel initialized")
    }

    fun fetchNextPokemonList() {
        Timber.d("fetchNextPokemonList index: $pokemonFetchingIndex loading: ${_isLoading.value}")
        if (_isLoading.value != true) {
            pokemonFetchingIndex++
            fetchPokemonList(pokemonFetchingIndex)
        }
    }

    private fun fetchPokemonList(page: Int) {
        viewModelScope.launch {
            EspressoIdlingResource.increment()

            fetchPokemonListUseCase(page)
                .onStart { onFetchStart() }
                .catch { onFetchError(it) }
                .collect { list -> onFetchSuccess(list) }

            onFetchComplete()
            EspressoIdlingResource.decrement()
        }
    }

    private fun onFetchStart() {
        _isLoading.value = true
        _isFetchFailed.value = false
    }

    private fun onFetchError(exception: Throwable) {
        Timber.e(exception, "Error fetching Pokemon list")
        _toastMessage.value = Event("Failed to load Pokémon list")
        _isFetchFailed.value = true
    }

    private fun onFetchSuccess(list: List<Pokemon>) {
        Timber.d("Fetched ${list.size} Pokémon items ${list.map { it.name }}")
        _pokemonList.value = _pokemonList.value.orEmpty().plus(list)
        Timber.d("Total Pokémon items: ${_pokemonList.value?.size}")
    }

    private fun onFetchComplete() {
        _isLoading.value = false
        if (_pokemonList.value.isNullOrEmpty()) {
            _isFetchFailed.value = true
            _toastMessage.value = Event("Failed to load Pokémon list")
        }
    }
}
