/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.data.di

import android.content.Context
import androidx.room.Room
import com.github.orioonyx.pokedex.data.local.PokeDexDatabase
import com.github.orioonyx.pokedex.data.local.dao.PokemonDao
import com.github.orioonyx.pokedex.data.local.dao.PokemonDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePokeDexDatabase(@ApplicationContext context: Context): PokeDexDatabase {
        return Room.databaseBuilder(
            context,
            PokeDexDatabase::class.java,
            "poke_dex_database"
        ).build()
    }

    @Provides
    fun providePokemonDao(database: PokeDexDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides
    fun providePokemonDetailDao(database: PokeDexDatabase): PokemonDetailDao {
        return database.pokemonDetailDao()
    }
}