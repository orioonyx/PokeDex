package com.github.orioonyx.pokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonEntity(
    val page: Int,
    @PrimaryKey val name: String,
    val url: String,
)
