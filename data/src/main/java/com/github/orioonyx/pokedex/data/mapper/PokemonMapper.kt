package com.github.orioonyx.pokedex.data.mapper

import com.github.orioonyx.pokedex.data.local.entity.PokemonDetailEntity
import com.github.orioonyx.pokedex.data.local.entity.PokemonEntity
import com.github.orioonyx.pokedex.data.remote.model.PokemonDetailDto
import com.github.orioonyx.pokedex.data.remote.model.PokemonDto
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.model.PokemonDetail.TypeResponse

object PokemonMapper {

    // Converts PokemonDto to Domain model Pokemon with a page number
    fun toDomain(dto: PokemonDto, page: Int): Pokemon {
        return Pokemon(
            page = page,
            name = dto.name,
            url = dto.url
        )
    }

    // Converts PokemonEntity to Domain model Pokemon
    fun toDomain(entity: PokemonEntity): Pokemon {
        return Pokemon(
            page = entity.page,
            name = entity.name,
            url = entity.url
        )
    }

    // Converts Domain model Pokemon to PokemonEntity for database storage
    fun toEntity(pokemon: Pokemon): PokemonEntity {
        return PokemonEntity(
            page = pokemon.page,
            name = pokemon.name,
            url = pokemon.url
        )
    }

    // Converts PokemonDetailDto to Domain model PokemonDetail
    fun toDomain(dto: PokemonDetailDto): PokemonDetail {
        return PokemonDetail(
            id = dto.id,
            name = dto.name,
            height = dto.height,
            weight = dto.weight,
            experience = dto.experience,
            types = dto.types.map {
                TypeResponse(
                    slot = it.slot,
                    type = PokemonDetail.Type(name = it.type.name)
                )
            }
        )
    }

    // Converts PokemonDetailEntity to Domain model PokemonDetail
    fun toDomain(entity: PokemonDetailEntity): PokemonDetail {
        return PokemonDetail(
            id = entity.id,
            name = entity.name,
            height = entity.height,
            weight = entity.weight,
            experience = entity.experience,
            types = entity.types.split(",").map {
                TypeResponse(0, PokemonDetail.Type(it))
            },
            hp = entity.hp,
            attack = entity.attack,
            defense = entity.defense,
            speed = entity.speed,
            exp = entity.exp
        )
    }

    // Converts Domain model PokemonDetail to PokemonDetailEntity for database storage
    fun toEntity(detail: PokemonDetail): PokemonDetailEntity {
        return PokemonDetailEntity(
            id = detail.id,
            name = detail.name,
            height = detail.height,
            weight = detail.weight,
            experience = detail.experience,
            types = detail.types.joinToString(",") { it.type.name },
            hp = detail.hp,
            attack = detail.attack,
            defense = detail.defense,
            speed = detail.speed,
            exp = detail.exp
        )
    }
}
