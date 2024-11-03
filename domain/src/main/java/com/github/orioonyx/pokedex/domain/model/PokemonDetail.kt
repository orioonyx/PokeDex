package com.github.orioonyx.pokedex.domain.model

import com.github.orioonyx.pokedex.core.network.PokemonConstants
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.random.Random

@JsonClass(generateAdapter = true)
data class PokemonDetail(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name") val name: String, // 이름
    @field:Json(name = "height") val height: Int, // 키
    @field:Json(name = "weight") val weight: Int, // 몸무게
    @field:Json(name = "base_experience") val experience: Int, // 포획 시 경험치
    @field:Json(name = "types") val types: List<TypeResponse>, // 타입
    val hp: Int = Random.nextInt(PokemonConstants.MAX_HP), // 체력
    val attack: Int = Random.nextInt(PokemonConstants.MAX_ATTACK), // 공격력
    val defense: Int = Random.nextInt(PokemonConstants.MAX_DEFENSE), // 방어력
    val speed: Int = Random.nextInt(PokemonConstants.MAX_SPEED), // 스피드
    val exp: Int = Random.nextInt(PokemonConstants.MAX_EXP), // 경험치
) {
    fun getIdString(): String = String.format(PokemonConstants.ID_FORMAT, id)
    fun getWeightString(): String = String.format(PokemonConstants.WEIGHT_FORMAT, weight.toFloat() / 10)
    fun getHeightString(): String = String.format(PokemonConstants.HEIGHT_FORMAT, height.toFloat() / 10)
    fun getHpString(): String = " $hp/${PokemonConstants.MAX_HP}"
    fun getAttackString(): String = " $attack/${PokemonConstants.MAX_ATTACK}"
    fun getDefenseString(): String = " $defense/${PokemonConstants.MAX_DEFENSE}"
    fun getSpeedString(): String = " $speed/${PokemonConstants.MAX_SPEED}"
    fun getExpString(): String = " $exp/${PokemonConstants.MAX_EXP}"

    @JsonClass(generateAdapter = true)
    data class TypeResponse(
        @field:Json(name = "slot") val slot: Int, // 타입 순서
        @field:Json(name = "type") val type: Type,
    )

    @JsonClass(generateAdapter = true)
    data class Type(
        @field:Json(name = "name") val name: String, // 타입 이름
    )
}
