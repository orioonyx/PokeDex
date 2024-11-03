package com.github.orioonyx.pokedex.data.remote.service

import com.github.orioonyx.pokedex.core.network.PokemonConstants.PAGING_SIZE
import com.github.orioonyx.pokedex.data.remote.model.PokemonDetailDto
import com.github.orioonyx.pokedex.data.remote.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class PokemonApiClient @Inject constructor(
    private val apiService: PokemonApiService
) {

    suspend fun fetchPokemonList(page: Int): ApiResponse<PokemonResponse> =
        apiService.fetchPokemonList(
            limit = PAGING_SIZE,
            offset = page * PAGING_SIZE
        )

    suspend fun fetchPokemonDetail(name: String): ApiResponse<PokemonDetailDto> =
        apiService.fetchPokemonDetail(name)
}
