package com.github.orioonyx.pokedex.data.remote.model.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDto(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "url") val url: String,
)
