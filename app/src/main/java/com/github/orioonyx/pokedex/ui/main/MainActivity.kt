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
import androidx.lifecycle.lifecycleScope
import com.github.orioonyx.pokedex.databinding.ActivityMainBinding
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.ui.detail.DetailActivity
import com.github.orioonyx.pokedex.ui.detail.DetailActivity.Companion.EXTRA_POKEMON
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var binding: ActivityMainBinding

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val viewModel: MainViewModel by viewModels()

    private val adapter by lazy { PokemonAdapter { pokemon -> onPokemonClick(pokemon) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            vm = viewModel
            this.adapter = this@MainActivity.adapter
        }
        setContentView(binding.root)

        observeViewModel()
        viewModel.fetchNextPokemonList()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.pokemonList.collectLatest { pokemonList ->
                adapter.submitList(pokemonList)
            }
        }

        lifecycleScope.launch {
            viewModel.toastMessage.collectLatest { event ->
                event?.getContentIfNotHandled()?.let { message ->
                    showToast(message)
                }
            }
        }
    }

    private fun onPokemonClick(pokemon: Pokemon) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra(EXTRA_POKEMON, pokemon)
        }.also { startActivity(it) }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}