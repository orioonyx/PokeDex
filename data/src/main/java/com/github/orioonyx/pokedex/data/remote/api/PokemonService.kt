/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

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
