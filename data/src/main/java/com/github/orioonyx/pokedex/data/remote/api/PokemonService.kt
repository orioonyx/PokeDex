package com.github.orioonyx.pokedex.data.remote.api

import com.github.orioonyx.pokedex.data.remote.model.dto.PokemonDetailDto
import com.github.orioonyx.pokedex.data.remote.model.response.PokemonResponse
import com.github.orioonyx.pokedex.data.remote.utils.PokemonConstants.PAGING_SIZE
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
