/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.data.mapper

import com.github.orioonyx.pokedex.data.local.entity.PokemonDetailEntity
import com.github.orioonyx.pokedex.data.local.entity.PokemonEntity
import com.github.orioonyx.pokedex.data.remote.model.dto.PokemonDetailDto
import com.github.orioonyx.pokedex.data.remote.model.dto.PokemonDto
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.data.local.entity.PokemonDetailEntity.PokemonTypeEntity as EntityPokemonTypeEntity
import com.github.orioonyx.pokedex.domain.model.PokemonDetail.PokemonType as DomainPokemonType

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
            types = dto.types.map { it.toDomainPokemonType() }
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
            types = entity.types.map { it.toDomainPokemonType() },
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
            types = detail.types.map { it.toEntityPokemonType() },
            hp = detail.hp,
            attack = detail.attack,
            defense = detail.defense,
            speed = detail.speed,
            exp = detail.exp
        )
    }

    // Extension function to convert DTO Type to Domain PokemonType
    private fun PokemonDetailDto.TypeResponse.toDomainPokemonType() = DomainPokemonType(
        slot = this.slot,
        type = PokemonDetail.TypeInfo(name = this.type.name)
    )

    // Extension function to convert Entity PokemonTypeEntity to Domain PokemonType
    private fun EntityPokemonTypeEntity.toDomainPokemonType() = DomainPokemonType(
        slot = this.slot,
        type = PokemonDetail.TypeInfo(name = this.type.name)
    )

    // Extension function to convert Domain PokemonType to Entity PokemonTypeEntity
    private fun DomainPokemonType.toEntityPokemonType() = EntityPokemonTypeEntity(
        slot = this.slot,
        type = PokemonDetailEntity.TypeInfo(name = this.type.name)
    )
}
