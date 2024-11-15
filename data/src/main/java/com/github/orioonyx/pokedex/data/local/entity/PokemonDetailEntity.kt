/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val experience: Int,
    val types: List<PokemonTypeEntity>,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val exp: Int
) {
    @JsonClass(generateAdapter = true)
    data class PokemonTypeEntity(
        val slot: Int,
        val type: TypeInfo
    )

    @JsonClass(generateAdapter = true)
    data class TypeInfo(
        val name: String
    )
}


