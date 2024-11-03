package com.github.orioonyx.pokedex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailDto(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "height") val height: Int,
    @field:Json(name = "weight") val weight: Int,
    @field:Json(name = "base_experience") val experience: Int,
    @field:Json(name = "types") val types: List<TypeResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class TypeResponse(
        @field:Json(name = "slot") val slot: Int,
        @field:Json(name = "type") val type: Type,
    )

    @JsonClass(generateAdapter = true)
    data class Type(
        @field:Json(name = "name") val name: String,
    )
}
