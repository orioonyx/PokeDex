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
