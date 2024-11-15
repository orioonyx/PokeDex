/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.data.local

import androidx.room.TypeConverter
import com.github.orioonyx.pokedex.data.local.entity.PokemonDetailEntity.PokemonTypeEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class TypeResponseConverter {
    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, PokemonTypeEntity::class.java)
    private val adapter = moshi.adapter<List<PokemonTypeEntity>>(type)

    @TypeConverter
    fun fromTypeResponseList(types: List<PokemonTypeEntity>): String {
        return adapter.toJson(types)
    }

    @TypeConverter
    fun toTypeResponseList(json: String): List<PokemonTypeEntity> {
        return adapter.fromJson(json) ?: emptyList()
    }
}
