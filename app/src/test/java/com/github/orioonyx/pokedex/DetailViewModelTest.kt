/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unit tests for DetailViewModel to verify fetching and error handling.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.core_test.TestDispatcherProvider
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
import com.github.orioonyx.pokedex.ui.detail.DetailViewModel
import com.github.orioonyx.pokedex.utils.ERROR_LOADING_POKEMON_DETAILS
import com.github.orioonyx.pokedex.utils.Event
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class DetailViewModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val fetchPokemonDetailUseCase: FetchPokemonDetailUseCase = mockk()

    @Inject
    lateinit var testDispatcherProvider: TestDispatcherProvider

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = DetailViewModel(fetchPokemonDetailUseCase)
    }

    @Test
    fun `fetchPokemonDetail should fetch and update pokemon detail`() = runTest(testDispatcherProvider.main) {
        // Given
        val pokemonDetail = MockUtil.mockPokemonDetail()
        coEvery { fetchPokemonDetailUseCase.invoke(any()) } returns flow { emit(pokemonDetail) }

        val observer: Observer<PokemonDetail?> = mockk(relaxed = true)
        viewModel.pokemonDetail.observeForever(observer)

        // When
        viewModel.fetchPokemonDetail("bulbasaur")

        // Then
        coVerify { observer.onChanged(pokemonDetail) }
        assertEquals(pokemonDetail, viewModel.pokemonDetail.value)

        viewModel.pokemonDetail.removeObserver(observer)
    }

    @Test
    fun `fetchPokemonDetail should show toast on error`() = runTest(testDispatcherProvider.main) {
        // Given
        val errorMessage = ERROR_LOADING_POKEMON_DETAILS
        coEvery { fetchPokemonDetailUseCase.invoke(any()) } returns flow { throw Exception(errorMessage) }

        val toastObserver: Observer<Event<String>> = mockk(relaxed = true)
        viewModel.toastMessage.observeForever(toastObserver)

        // When
        viewModel.fetchPokemonDetail("bulbasaur")

        // Then
        coVerify {
            toastObserver.onChanged(match { it.peekContent() == errorMessage })
        }

        viewModel.toastMessage.removeObserver(toastObserver)
    }
}
