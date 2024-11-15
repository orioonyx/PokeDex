/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.orioonyx.pokedex.data.local.dao.PokemonDao
import com.github.orioonyx.pokedex.data.local.dao.PokemonDetailDao
import com.github.orioonyx.pokedex.data.local.entity.PokemonDetailEntity
import com.github.orioonyx.pokedex.data.local.entity.PokemonEntity

@Database(entities = [PokemonEntity::class, PokemonDetailEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeResponseConverter::class)
abstract class PokeDexDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonDetailDao(): PokemonDetailDao
}
