/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.domain.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Pokemon(
    var page: Int = 0,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "url") val url: String,
) : Parcelable {

    fun name(): String = name.replaceFirstChar { it.uppercase() }

    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return getPokemonSpriteUrl(index.toInt())
    }

    fun getGifUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return getPokemonGifUrl(index.toInt())
    }

    fun getBackGifUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return getPokemonBackGifUrl(index.toInt())
    }

    fun getShinyGifUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return getPokemonShinyGifUrl(index.toInt())
    }

    fun getShinyBackGifUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return getPokemonShinyBackGifUrl(index.toInt())
    }

    companion object {
        private const val SPRITES_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/"

        fun getPokemonSpriteUrl(id: Int) = "${SPRITES_URL}pokemon/other/official-artwork/$id.png"
        fun getPokemonGifUrl(id: Int) = "${SPRITES_URL}pokemon/other/showdown/$id.gif"
        fun getPokemonBackGifUrl(id: Int) = "${SPRITES_URL}pokemon/other/showdown/back/$id.gif"
        fun getPokemonShinyGifUrl(id: Int) = "${SPRITES_URL}pokemon/other/showdown/shiny/$id.gif"
        fun getPokemonShinyBackGifUrl(id: Int) =
            "${SPRITES_URL}pokemon/other/showdown/back/shiny/$id.gif"
    }
}
