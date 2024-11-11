/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.utils

import com.github.orioonyx.pokedex.R

object PokemonTypeUtils {

    private val typeColorMap = mapOf(
        "fighting" to R.color.fighting,
        "flying" to R.color.flying,
        "poison" to R.color.poison,
        "ground" to R.color.ground,
        "rock" to R.color.rock,
        "bug" to R.color.bug,
        "ghost" to R.color.ghost,
        "steel" to R.color.steel,
        "fire" to R.color.fire,
        "water" to R.color.water,
        "grass" to R.color.grass,
        "electric" to R.color.electric,
        "psychic" to R.color.psychic,
        "ice" to R.color.ice,
        "dragon" to R.color.dragon,
        "fairy" to R.color.fairy,
        "dark" to R.color.dark
    )

    fun getTypeColor(type: String): Int {
        return typeColorMap[type] ?: R.color.dark
    }
}
