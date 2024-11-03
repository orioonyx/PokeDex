package com.github.orioonyx.pokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.orioonyx.pokedex.data.local.entity.PokemonDetailEntity

@Dao
interface PokemonDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(pokemonDetail: PokemonDetailEntity)

    @Query("SELECT * FROM PokemonDetailEntity WHERE name = :name")
    suspend fun getPokemonDetail(name: String): PokemonDetailEntity?
}