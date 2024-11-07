package com.github.orioonyx.pokedex.data.remote.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonErrorResponse(
    val code: Int? = null,
    val message: String? = null,
)
