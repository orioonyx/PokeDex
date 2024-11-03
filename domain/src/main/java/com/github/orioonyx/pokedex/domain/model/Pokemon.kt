package com.github.orioonyx.pokedex.domain.model

import android.os.Parcelable
import com.github.orioonyx.pokedex.core.network.PokemonConstants
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Pokemon(
    var page: Int = 0,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "url") val url: String,
) : Parcelable {

    fun name(): String = name.replaceFirstChar { it.uppercase() }

    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return PokemonConstants.getPokemonSpriteUrl(index.toInt())
    }

    fun getGifUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return PokemonConstants.getPokemonGifUrl(index.toInt())
    }
}
