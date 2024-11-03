package com.github.orioonyx.pokedex.data.remote.service

import com.github.orioonyx.pokedex.core.network.PokemonConstants.PAGING_SIZE
import com.github.orioonyx.pokedex.data.remote.model.PokemonDetailDto
import com.github.orioonyx.pokedex.data.remote.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = PAGING_SIZE,
        @Query("offset") offset: Int = 0
    ): ApiResponse<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonDetail(
        @Path("name") name: String
    ): ApiResponse<PokemonDetailDto>
}
