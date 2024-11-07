package com.github.orioonyx.pokedex.data.remote.api

import com.github.orioonyx.pokedex.data.mapper.ErrorResponseMapper
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.map
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import javax.inject.Inject

class ApiResponseHandler @Inject constructor(
    private val errorResponseMapper: ErrorResponseMapper
) {
    suspend fun <T> handle(
        response: ApiResponse<T>,
        onSuccess: suspend (T) -> Unit
    ) {
        response.suspendOnSuccess {
            onSuccess(data)
        }.onError {
            map(errorResponseMapper) { throw Exception("[Error Code: $code]: $message") }
        }.onFailure {
            throw Exception("Network Failure: ${message()}")
        }.onException {
            throw Exception("Exception: ${throwable.message}")
        }
    }
}
