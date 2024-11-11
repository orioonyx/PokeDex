/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.data.repository

import androidx.annotation.WorkerThread
import com.github.orioonyx.pokedex.data.di.IoDispatcher
import com.github.orioonyx.pokedex.data.local.dao.PokemonDetailDao
import com.github.orioonyx.pokedex.data.mapper.PokemonMapper
import com.github.orioonyx.pokedex.data.remote.api.ApiResponseHandler
import com.github.orioonyx.pokedex.data.remote.api.PokemonApiClient
import com.github.orioonyx.pokedex.domain.model.PokemonDetail
import com.github.orioonyx.pokedex.domain.repository.PokemonDetailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PokemonDetailRepositoryImpl @Inject constructor(
    private val apiClient: PokemonApiClient,
    private val pokemonDetailDao: PokemonDetailDao,
    private val apiResponseHandler: ApiResponseHandler,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PokemonDetailRepository {

    @WorkerThread
    override fun fetchPokemonDetail(name: String): Flow<PokemonDetail> = flow {
        val cachedDetail = pokemonDetailDao.getPokemonDetail(name)
        if (cachedDetail != null) {
            emit(PokemonMapper.toDomain(cachedDetail))
        } else {
            val response = apiClient.fetchPokemonDetail(name)
            apiResponseHandler.handle(response) { data ->
                val pokemonDetail = PokemonMapper.toDomain(data)
                pokemonDetailDao.insertPokemonDetail(PokemonMapper.toEntity(pokemonDetail))
                emit(pokemonDetail)
            }
        }
    }.catch { exception ->
        throw Exception("Exception in flow: ${exception.message}")
    }.flowOn(ioDispatcher)
}


