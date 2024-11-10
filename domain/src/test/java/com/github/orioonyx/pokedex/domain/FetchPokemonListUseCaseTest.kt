/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unit tests for FetchPokemonListUseCase to validate success and failure cases.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.domain

import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.repository.PokemonListRepository
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import timber.log.Timber

class FetchPokemonListUseCaseTest {

    private val repository: PokemonListRepository = mockk()
    private lateinit var useCase: FetchPokemonListUseCase

    @Before
    fun setup() {
        useCase = FetchPokemonListUseCase(repository)
    }

    @Test
    fun `fetchPokemonList returns list`() = runTest {
        // Given
        val expectedList = listOf(MockUtil.mockPokemon())
        coEvery { repository.fetchPokemonList(any()) } returns flow { emit(expectedList) }

        // When
        val result: Flow<List<Pokemon>> = useCase(1)

        // Then
        result.collect { list ->
            assertEquals(expectedList, list)
        }
    }

    @Test
    fun `fetchPokemonList logs error when failed`() = runTest {
        // Given
        val errorMessage = "Error fetching Pokemon list"
        coEvery { repository.fetchPokemonList(any()) } returns flow { throw Exception(errorMessage) }

        // When & Then
        useCase(1)
            .catch { e ->
                assertTrue(e is Exception && e.message == errorMessage)
                Timber.e(e)
            }
            .collect()
    }
}
