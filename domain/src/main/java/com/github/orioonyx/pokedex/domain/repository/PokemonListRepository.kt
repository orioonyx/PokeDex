/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.domain.repository

import androidx.annotation.WorkerThread
import com.github.orioonyx.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonListRepository {

    @WorkerThread
    fun fetchPokemonList(page: Int): Flow<List<Pokemon>>
}