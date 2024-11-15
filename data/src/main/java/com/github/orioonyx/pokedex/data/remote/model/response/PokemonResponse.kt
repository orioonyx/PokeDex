/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.data.remote.model.response

import com.github.orioonyx.pokedex.data.remote.model.dto.PokemonDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse(
    @field:Json(name = "count") val count: Int,
    @field:Json(name = "next") val next: String?,
    @field:Json(name = "previous") val previous: String?,
    @field:Json(name = "results") val results: List<PokemonDto>,
)