/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unit tests for MainViewModel to verify fetching and error handling.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex

import app.cash.turbine.test
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.core_test.TestDispatcherProvider
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.ui.main.MainViewModel
import com.github.orioonyx.pokedex.utils.ERROR_LOADING_POKEMON_LIST
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = androidx.arch.core.executor.testing.InstantTaskExecutorRule()

    private val fetchPokemonListUseCase: FetchPokemonListUseCase = mockk()
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = TestDispatcherProvider.main

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(fetchPokemonListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchNextPokemonList updates pokemonList on success`() = runTest(testDispatcher) {
        // Given
        val mockPokemonList = MockUtil.generatePokemonList(5)
        coEvery { fetchPokemonListUseCase(any()) } returns flow { emit(mockPokemonList) }

        // When
        viewModel.fetchNextPokemonList()

        // Then
        viewModel.pokemonList.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(mockPokemonList, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
        }
        viewModel.isFetchFailed.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `fetchNextPokemonList shows toast on error`() = runTest(testDispatcher) {
        // Given
        coEvery { fetchPokemonListUseCase(any()) } returns flow { throw RuntimeException("Error") }

        // When
        viewModel.fetchNextPokemonList()

        // Then
        viewModel.toastMessage.test {
            assertEquals(null, awaitItem())
            assertEquals(ERROR_LOADING_POKEMON_LIST, awaitItem()?.peekContent())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
        }
        viewModel.isFetchFailed.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `fetchNextPokemonList does not fetch when already loading`() = runTest(testDispatcher) {
        // Given
        val mockPokemonList = MockUtil.generatePokemonList(5)
        coEvery { fetchPokemonListUseCase(any()) } returns flow { emit(mockPokemonList) }

        // When
        viewModel.fetchNextPokemonList()
        viewModel.fetchNextPokemonList()

        // Then
        viewModel.pokemonList.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(mockPokemonList, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
        }
    }
}
