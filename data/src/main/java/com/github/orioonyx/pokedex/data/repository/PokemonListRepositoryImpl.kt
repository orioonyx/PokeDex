package com.github.orioonyx.pokedex.data.repository

import com.github.orioonyx.pokedex.data.di.IoDispatcher
import com.github.orioonyx.pokedex.data.local.dao.PokemonDao
import com.github.orioonyx.pokedex.data.mapper.ErrorResponseMapper
import com.github.orioonyx.pokedex.data.mapper.PokemonMapper
import com.github.orioonyx.pokedex.data.remote.service.PokemonApiClient
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.repository.PokemonListRepository
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

class PokemonListRepositoryImpl @Inject constructor(
    private val apiClient: PokemonApiClient,
    private val pokemonDao: PokemonDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PokemonListRepository {

    override fun fetchPokemonList(page: Int): Flow<List<Pokemon>> = flow {
        // Attempt to load from cache first
        val cachedPokemonList = pokemonDao.getPokemonList(page)
        if (cachedPokemonList.isNotEmpty()) {
            // Convert cached entities to domain models and emit
            emit(cachedPokemonList.map { PokemonMapper.toDomain(it) })
        } else {
            // Fetch from API if cache is empty
            val response = apiClient.fetchPokemonList(page)
            response.suspendOnSuccess {
                // Convert API response to domain models and cache in the database
                val pokemonList = data.results.map { PokemonMapper.toDomain(it, page) }
                pokemonDao.insertPokemonList(pokemonList.map { PokemonMapper.toEntity(it) })
                emit(pokemonList)
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

