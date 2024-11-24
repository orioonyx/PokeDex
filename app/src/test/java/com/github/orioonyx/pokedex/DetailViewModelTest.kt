/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unit tests for DetailViewModel to verify fetching and error handling.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex

import app.cash.turbine.test
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.core_test.TestDispatcherProvider
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
import com.github.orioonyx.pokedex.ui.detail.DetailViewModel
import com.github.orioonyx.pokedex.utils.ERROR_LOADING_POKEMON_DETAILS
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    private val fetchPokemonDetailUseCase: FetchPokemonDetailUseCase = mockk()
    private lateinit var viewModel: DetailViewModel
    private val testDispatcher = TestDispatcherProvider.main

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailViewModel(fetchPokemonDetailUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchPokemonDetail updates pokemonDetail on success`() = runTest(testDispatcher) {
        // Given
        val mockDetail = MockUtil.mockPokemonDetail()
        coEvery { fetchPokemonDetailUseCase("ditto") } returns flow { emit(mockDetail) }

        // When
        viewModel.fetchPokemonDetail("ditto")

        // Then
        viewModel.pokemonDetail.test {
            assertEquals(MockUtil.emptyPokemonDetail(), awaitItem())
            assertEquals(mockDetail, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isFetchFailed.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchPokemonDetail shows toast on error`() = runTest(testDispatcher) {
        // Given
        coEvery { fetchPokemonDetailUseCase("ditto") } returns flow { throw RuntimeException("Error") }

        // When
        viewModel.fetchPokemonDetail("ditto")

        // Then
        viewModel.toastMessage.test {
            assertEquals(null, awaitItem())
            assertEquals(ERROR_LOADING_POKEMON_DETAILS, awaitItem()?.peekContent())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isFetchFailed.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchPokemonDetail handles empty detail`() = runTest(testDispatcher) {
        // Given
        val emptyDetail = MockUtil.emptyPokemonDetail()
        coEvery { fetchPokemonDetailUseCase("unknown") } returns flow { emit(emptyDetail) }

        // When
        viewModel.fetchPokemonDetail("unknown")
        advanceUntilIdle()

        // Then
        viewModel.pokemonDetail.test {
            assertEquals(emptyDetail, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isFetchFailed.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
