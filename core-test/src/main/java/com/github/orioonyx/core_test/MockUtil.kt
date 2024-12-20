/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Utility functions to provide mock data for unit testing of Pokemon models.
 * Includes methods for creating mock Pokemon and PokemonDetail instances.
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.core_test

import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.model.PokemonDetail

object MockUtil {

    fun mockPokemon(
        name: String = "ditto",
        url: String = "https://pokeapi.co/api/v2/pokemon/1/",
        page: Int = 1
    ): Pokemon = Pokemon(
        page = page,
        name = name,
        url = url
    )

    fun generatePokemonList(
        count: Int,
        startIndex: Int = 1,
        overrideFirst: (Pokemon.() -> Pokemon)? = null
    ): List<Pokemon> =
        List(count) { index ->
            val actualIndex = startIndex + index
            val defaultPokemon = mockPokemon(
                name = "pokemon_$actualIndex",
                url = "https://pokeapi.co/api/v2/pokemon/$actualIndex/",
                page = (actualIndex - 1) / 20 + 1
            )
            if (index == 0 && overrideFirst != null) {
                overrideFirst(defaultPokemon)
            } else {
                defaultPokemon
            }
        }

    fun mockPokemonDetail(
        name: String = "ditto",
        types: List<PokemonDetail.PokemonType> = listOf(
            PokemonDetail.PokemonType(slot = 1, type = PokemonDetail.TypeInfo(name = "normal")),
            PokemonDetail.PokemonType(slot = 2, type = PokemonDetail.TypeInfo(name = "psychic"))
        )
    ): PokemonDetail = PokemonDetail(
        id = 1,
        name = name,
        height = 3,
        weight = 40,
        experience = 101,
        types = types,
        hp = 48,
        attack = 55,
        defense = 50,
        speed = 40,
        exp = 101
    )

    fun emptyPokemonDetail(): PokemonDetail = PokemonDetail(
        id = 0,
        name = "",
        height = 0,
        weight = 0,
        experience = 0,
        types = emptyList(),
        hp = 0,
        attack = 0,
        defense = 0,
        speed = 0,
        exp = 0
    )
}