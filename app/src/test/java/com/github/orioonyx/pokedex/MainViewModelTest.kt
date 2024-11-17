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
import com.github.orioonyx.core_test.TestDispatcherProvider
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import com.github.orioonyx.pokedex.ui.main.MainViewModel
import com.github.orioonyx.pokedex.utils.ERROR_LOADING_POKEMON_LIST
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
class MainViewModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val fetchPokemonListUseCase: FetchPokemonListUseCase = mockk()

    @Inject
    lateinit var testDispatcherProvider: TestDispatcherProvider

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = MainViewModel(fetchPokemonListUseCase)
    }

    @Test
    fun `fetchNextPokemonList should fetch and update pokemon list`() = runTest(testDispatcherProvider.main) {
        // Given
        val pokemonList = listOf(MockUtil.mockPokemon())
        coEvery { fetchPokemonListUseCase.invoke(any()) } returns flow { emit(pokemonList) }

        val observer: Observer<List<Pokemon>> = mockk(relaxed = true)
        viewModel.pokemonList.observeForever(observer)

        // When
        viewModel.fetchNextPokemonList()

        // Then
        assertEquals(pokemonList, viewModel.pokemonList.value)
        coVerify { observer.onChanged(pokemonList) }

        viewModel.pokemonList.removeObserver(observer)
    }

    @Test
    fun `fetchNextPokemonList should show toast on error`() = runTest(testDispatcherProvider.main) {
        // Given
        val errorMessage = ERROR_LOADING_POKEMON_LIST
        coEvery { fetchPokemonListUseCase.invoke(any()) } returns flow { throw Exception(errorMessage) }

        val toastObserver: Observer<Event<String>> = mockk(relaxed = true)
        viewModel.toastMessage.observeForever(toastObserver)

        // When
        viewModel.fetchNextPokemonList()

        // Then
        coVerify {
            toastObserver.onChanged(match { it.peekContent() == errorMessage })
        }

        viewModel.toastMessage.removeObserver(toastObserver)
    }
}
