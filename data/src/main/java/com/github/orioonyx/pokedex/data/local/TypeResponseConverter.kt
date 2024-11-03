package com.github.orioonyx.pokedex.data.local

import androidx.room.TypeConverter
import com.github.orioonyx.pokedex.data.local.entity.PokemonDetailEntity.TypeResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class TypeResponseConverter {
    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, TypeResponse::class.java)
    private val adapter = moshi.adapter<List<TypeResponse>>(type)

    @TypeConverter
    fun fromTypeResponseList(types: List<TypeResponse>): String {
        return adapter.toJson(types)
    }

    @TypeConverter
    fun toTypeResponseList(json: String): List<TypeResponse> {
        return adapter.fromJson(json) ?: emptyList()
    }
}
