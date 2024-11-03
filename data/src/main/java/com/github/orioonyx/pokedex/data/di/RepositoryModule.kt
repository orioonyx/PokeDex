package com.github.orioonyx.pokedex.data.di

import com.github.orioonyx.pokedex.data.repository.PokemonDetailRepositoryImpl
import com.github.orioonyx.pokedex.data.repository.PokemonListRepositoryImpl
import com.github.orioonyx.pokedex.domain.repository.PokemonDetailRepository
import com.github.orioonyx.pokedex.domain.repository.PokemonListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPokemonListRepository(
        repository: PokemonListRepositoryImpl
    ): PokemonListRepository

    @Binds
    @Singleton
    abstract fun bindPokemonDetailRepository(
        repository: PokemonDetailRepositoryImpl
    ): PokemonDetailRepository

}