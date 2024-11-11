/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.data.remote.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonErrorResponse(
    val code: Int? = null,
    val message: String? = null,
)
