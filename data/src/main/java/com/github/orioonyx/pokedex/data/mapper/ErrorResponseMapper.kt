package com.github.orioonyx.pokedex.data.mapper

import com.github.orioonyx.pokedex.data.remote.model.response.PokemonErrorResponse
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiErrorModelMapper
import com.skydoves.sandwich.message
import com.skydoves.sandwich.retrofit.statusCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorResponseMapper @Inject constructor() : ApiErrorModelMapper<PokemonErrorResponse> {

    override fun map(apiErrorResponse: ApiResponse.Failure.Error): PokemonErrorResponse {
        return PokemonErrorResponse(apiErrorResponse.statusCode.code, apiErrorResponse.message())
    }
}
