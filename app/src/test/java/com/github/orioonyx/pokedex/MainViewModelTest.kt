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
import androidx.lifecycle.Observer
import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.ui.main.MainViewModel
import com.github.orioonyx.pokedex.utils.Event
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val fetchPokemonListUseCase: FetchPokemonListUseCase = mockk()
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(fetchPokemonListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchNextPokemonList should fetch and update pokemon list`() = runTest(testDispatcher) {
        // Given
        val pokemonList = listOf(MockUtil.mockPokemon())
        coEvery { fetchPokemonListUseCase.invoke(any()) } returns flow { emit(pokemonList) }

        val observer: Observer<List<Pokemon>> = mockk(relaxed = true)
        viewModel.pokemonList.observeForever(observer)

        // When
        viewModel.fetchNextPokemonList()

        // Wait for coroutine execution to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(pokemonList, viewModel.pokemonList.value)
        coVerify { observer.onChanged(pokemonList) }

        // Cleanup
        viewModel.pokemonList.removeObserver(observer)
    }

    @Test
    fun `fetchNextPokemonList should show toast on error`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Failed to load Pokémon list"
        coEvery { fetchPokemonListUseCase.invoke(any()) } returns flow { throw Exception(errorMessage) }

        val toastObserver: Observer<Event<String>> = mockk(relaxed = true)
        viewModel.toastMessage.observeForever(toastObserver)

        // When
        viewModel.fetchNextPokemonList()

        // Wait for coroutine execution to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify {
            toastObserver.onChanged(match { it.peekContent() == errorMessage })
        }

        // Cleanup
        viewModel.toastMessage.removeObserver(toastObserver)
    }


}
