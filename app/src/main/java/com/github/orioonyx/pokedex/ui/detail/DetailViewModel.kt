package com.github.orioonyx.pokedex.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
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

            fetchPokemonDetailUseCase(name)
                .onStart { onFetchStart() }
                .catch { onFetchError(it) }
                .collect { detail -> onFetchSuccess(detail) }

            onFetchComplete()
            EspressoIdlingResource.decrement()
        }
    }

    private fun onFetchStart() {
        _isLoading.value = true
        _isFetchFailed.value = false
    }

    private fun onFetchError(exception: Throwable) {
        Timber.e(exception, "Error fetching Pokemon detail")
        _toastMessage.value = Event("Failed to load details")
        _isFetchFailed.value = true
    }

    private fun onFetchSuccess(detail: PokemonDetail?) {
        _pokemonDetail.value = detail
    }

    private fun onFetchComplete() {
        _isLoading.value = false
        if (_pokemonDetail.value == null || _pokemonDetail.value?.name.isNullOrEmpty()) {
            _isFetchFailed.value = true
            _toastMessage.value = Event("Failed to load details")
        }
    }
}
