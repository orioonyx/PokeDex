package com.github.orioonyx.pokedex.data.remote.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PokemonDto(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "url") val url: String,
) : Parcelable
