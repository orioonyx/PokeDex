/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.domain.model

import android.annotation.SuppressLint
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.random.Random

@JsonClass(generateAdapter = true)
data class PokemonDetail(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "height") val height: Int,
    @field:Json(name = "weight") val weight: Int,
    @field:Json(name = "base_experience") val experience: Int,
    @field:Json(name = "types") val types: List<PokemonType>,
    val hp: Int = Random.nextInt(MAX_HP),
    val attack: Int = Random.nextInt(MAX_ATTACK),
    val defense: Int = Random.nextInt(MAX_DEFENSE),
    val speed: Int = Random.nextInt(MAX_SPEED),
    val exp: Int = Random.nextInt(MAX_EXP),
) {
    @JsonClass(generateAdapter = true)
    data class PokemonType(
        @field:Json(name = "slot") val slot: Int,
        @field:Json(name = "type") val type: TypeInfo,
    )

    @JsonClass(generateAdapter = true)
    data class TypeInfo(
        @field:Json(name = "name") val name: String,
    )

    @SuppressLint("DefaultLocale")
    fun getWeightString(): String = String.format(WEIGHT_FORMAT, weight.toFloat() / 10)

    @SuppressLint("DefaultLocale")
    fun getHeightString(): String = String.format(HEIGHT_FORMAT, height.toFloat() / 10)

    companion object {
        const val MAX_HP = 300
        const val MAX_ATTACK = 300
        const val MAX_DEFENSE = 300
        const val MAX_SPEED = 300
        const val MAX_EXP = 1000

        const val WEIGHT_FORMAT = "%.1f KG"
        const val HEIGHT_FORMAT = "%.1f M"
    }
}
