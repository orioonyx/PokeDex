package com.github.orioonyx.pokedex.data.repository

import com.github.orioonyx.pokedex.data.di.IoDispatcher
import com.github.orioonyx.pokedex.data.local.dao.PokemonDao
import com.github.orioonyx.pokedex.data.mapper.PokemonMapper
import com.github.orioonyx.pokedex.data.remote.api.ApiResponseHandler
import com.github.orioonyx.pokedex.data.remote.api.PokemonApiClient
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.domain.repository.PokemonListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PokemonListRepositoryImpl @Inject constructor(
    private val apiClient: PokemonApiClient,
    private val pokemonDao: PokemonDao,
    private val apiResponseHandler: ApiResponseHandler,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PokemonListRepository {

    override fun fetchPokemonList(page: Int): Flow<List<Pokemon>> = flow {
        val cachedPokemonList = pokemonDao.getPokemonList(page)
        if (cachedPokemonList.isNotEmpty()) {
            emit(cachedPokemonList.map { PokemonMapper.toDomain(it) })
        } else {
            val response = apiClient.fetchPokemonList(page)
            apiResponseHandler.handle(response) { data ->
                val pokemonList = data.results.map { PokemonMapper.toDomain(it, page) }
                pokemonDao.insertPokemonList(pokemonList.map { PokemonMapper.toEntity(it) })
                emit(pokemonList)
            }
        }
    }.catch { exception ->
        throw Exception("Exception in flow: ${exception.message}")
    }.flowOn(ioDispatcher)
}


