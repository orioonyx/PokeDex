/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.github.orioonyx.pokedex.databinding.ActivityMainBinding
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.ui.detail.DetailActivity
import com.github.orioonyx.pokedex.ui.detail.DetailActivity.Companion.EXTRA_POKEMON
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @VisibleForTesting
    lateinit var binding: ActivityMainBinding

    @VisibleForTesting
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.vm = viewModel

        setupPokemonListObserver()
        setupAdapter()
        viewModel.fetchNextPokemonList()
    }

    private fun setupPokemonListObserver() {
        viewModel.pokemonList.observe(this) { pokemonList ->
            (binding.adapter as? PokemonAdapter)?.submitList(pokemonList)
        }
    }

    private fun setupAdapter() {
        val adapter = PokemonAdapter { pokemon -> onPokemonClick(pokemon) }
        binding.adapter = adapter
    }

    private fun onPokemonClick(pokemon: Pokemon) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(EXTRA_POKEMON, pokemon)
        }
        startActivity(intent)
    }
}
