package com.github.orioonyx.pokedex.data.repository

import androidx.annotation.WorkerThread
import com.github.orioonyx.pokedex.data.di.IoDispatcher
import com.github.orioonyx.pokedex.data.local.dao.PokemonDetailDao
import com.github.orioonyx.pokedex.data.mapper.ErrorResponseMapper
import com.github.orioonyx.pokedex.data.mapper.PokemonMapper
import com.github.orioonyx.pokedex.data.remote.service.PokemonApiClient
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.repository.PokemonDetailRepository
import com.skydoves.sandwich.map
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PokemonDetailRepositoryImpl @Inject constructor(
    private val apiClient: PokemonApiClient,
    private val pokemonDetailDao: PokemonDetailDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PokemonDetailRepository {

    @WorkerThread
    override fun fetchPokemonDetail(name: String): Flow<PokemonDetail> = flow {
        // Check cache for existing Pokemon detail data
        val cachedDetail = pokemonDetailDao.getPokemonDetail(name)
        if (cachedDetail != null) {
            emit(PokemonMapper.toDomain(cachedDetail))
        } else {
            // Make network request if no cached data is available
            val response = apiClient.fetchPokemonDetail(name)
            response.suspendOnSuccess {
                val pokemonDetail = PokemonMapper.toDomain(data)
                pokemonDetailDao.insertPokemonDetail(PokemonMapper.toEntity(pokemonDetail))
                emit(pokemonDetail)
            }.onError {
                map(ErrorResponseMapper) {
                    throw Exception("[Error Code: $code]: $message")
                }
            }.onFailure {
                throw Exception("Network Failure: ${message()}")
            }.onException {
                throw Exception("Exception: ${throwable.message}")
            }
        }
    }.catch { exception ->
        throw Exception("Exception in flow: ${exception.message}")
    }.flowOn(ioDispatcher)
}

