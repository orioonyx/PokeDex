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
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
import com.github.orioonyx.pokedex.ui.detail.DetailViewModel
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
class DetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val fetchPokemonDetailUseCase: FetchPokemonDetailUseCase = mockk()
    private lateinit var viewModel: DetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailViewModel(fetchPokemonDetailUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchPokemonDetail should fetch and update pokemon detail`() = runTest(testDispatcher) {
        // Given
        val pokemonDetail = MockUtil.mockPokemonDetail()
        coEvery { fetchPokemonDetailUseCase.invoke(any()) } returns flow { emit(pokemonDetail) }

        val observer: Observer<PokemonDetail?> = mockk(relaxed = true)
        viewModel.pokemonDetail.observeForever(observer)

        // When
        viewModel.fetchPokemonDetail("bulbasaur")

        // Wait for coroutine execution to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { observer.onChanged(pokemonDetail) }
        assertEquals(pokemonDetail, viewModel.pokemonDetail.value)

        // Cleanup
        viewModel.pokemonDetail.removeObserver(observer)
    }

    @Test
    fun `fetchPokemonDetail should show toast on error`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Failed to load details"
        coEvery { fetchPokemonDetailUseCase.invoke(any()) } returns flow { throw Exception(errorMessage) }

        val toastObserver: Observer<Event<String>> = mockk(relaxed = true)
        viewModel.toastMessage.observeForever(toastObserver)

        // When
        viewModel.fetchPokemonDetail("bulbasaur")

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
