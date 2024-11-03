package com.github.orioonyx.pokedex.domain.repository

import androidx.annotation.WorkerThread
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonDetailRepository {

    @WorkerThread
    fun fetchPokemonDetail(name: String): Flow<PokemonDetail>
}
