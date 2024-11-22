/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unit tests for MainViewModel to verify fetching and error handling.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

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
        advanceUntilIdle()

        // Then
        assertEquals(mockPokemonList, viewModel.pokemonList.value)
        assertFalse(viewModel.isLoading.value ?: true)
        assertFalse(viewModel.isFetchFailed.value ?: true)
    }

    @Test
    fun `fetchNextPokemonList shows toast on error`() = runTest(testDispatcher) {
        // Given
        coEvery { fetchPokemonListUseCase(any()) } returns flow { throw RuntimeException("Error") }

        // When
        viewModel.fetchNextPokemonList()
        advanceUntilIdle()

        // Then
        val actualToastMessage = viewModel.toastMessage.value?.peekContent()
        assertEquals(ERROR_LOADING_POKEMON_LIST, actualToastMessage)
        assertFalse(viewModel.isLoading.value ?: true)
        assertTrue(viewModel.isFetchFailed.value ?: false)
    }

    @Test
    fun `fetchNextPokemonList does not fetch when already loading`() = runTest(testDispatcher) {
        // Given
        val mockPokemonList = MockUtil.generatePokemonList(5)
        coEvery { fetchPokemonListUseCase(any()) } returns flow { emit(mockPokemonList) }

        // When
        viewModel.fetchNextPokemonList() // First fetch
        advanceUntilIdle()
        viewModel.fetchNextPokemonList() // Attempt second fetch during loading

        // Then
        assertEquals(mockPokemonList, viewModel.pokemonList.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }
}
