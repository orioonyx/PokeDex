/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.domain.usecase

import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.repository.PokemonListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import javax.inject.Inject

class FetchPokemonListUseCase @Inject constructor(
    private val repository: PokemonListRepository
) {
    operator fun invoke(page: Int): Flow<List<Pokemon>> {
        return repository.fetchPokemonList(page)
            .catch { exception ->
                Timber.e(exception, "Error fetching Pokemon list")
            }
    }
}