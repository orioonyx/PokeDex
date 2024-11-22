/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unit tests for FetchPokemonDetailUseCase to validate success and failure cases.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.domain

import com.github.orioonyx.core_test.MockUtil
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.repository.PokemonDetailRepository
import com.github.orioonyx.pokedex.domain.usecase.FetchPokemonDetailUseCase
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

class FetchPokemonDetailUseCaseTest {

    private val repository: PokemonDetailRepository = mockk()
    private lateinit var useCase: FetchPokemonDetailUseCase

    @Before
    fun setup() {
        useCase = FetchPokemonDetailUseCase(repository)
    }

    @Test
    fun `fetchPokemonDetail should return pokemon detail`() = runTest {
        // Given
        val pokemonDetail = MockUtil.mockPokemonDetail()
        coEvery { repository.fetchPokemonDetail("Pikachu") } returns flow { emit(pokemonDetail) }

        // When
        val result: Flow<PokemonDetail> = useCase("Pikachu")

        // Then
        result.collect { detail ->
            assertEquals(pokemonDetail, detail)
        }
    }

    @Test
    fun `fetchPokemonDetail should handle error gracefully`() = runTest {
        // Given
        val errorMessage = "Error fetching Pokemon detail"
        coEvery { repository.fetchPokemonDetail("Pikachu") } returns flow { throw Exception(errorMessage) }

        // When
        val result: Flow<PokemonDetail> = useCase("Pikachu")

        // Then
        result.catch { e ->
            assertTrue(e is Exception && e.message == errorMessage)
        }.collect()
    }
}
