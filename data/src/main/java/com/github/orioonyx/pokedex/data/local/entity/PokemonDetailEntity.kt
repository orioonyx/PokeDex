package com.github.orioonyx.pokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val experience: Int,
    val types: String,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val exp: Int
) {
    @JsonClass(generateAdapter = true)
    data class TypeResponse(
        val slot: Int,
        val type: Type
    )

    @JsonClass(generateAdapter = true)
    data class Type(
        val name: String
    )
}
