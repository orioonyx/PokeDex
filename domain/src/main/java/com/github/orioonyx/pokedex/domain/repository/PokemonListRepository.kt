package com.github.orioonyx.pokedex.domain.repository

import androidx.annotation.WorkerThread
import com.github.orioonyx.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonListRepository {

    @WorkerThread
    fun fetchPokemonList(page: Int): Flow<List<Pokemon>>
}