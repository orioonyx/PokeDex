/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.ui.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.orioonyx.pokedex.R
import com.github.orioonyx.pokedex.databinding.ActivityDetailBinding
import com.github.orioonyx.pokedex.domain.model.Pokemon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var binding: ActivityDetailBinding

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val viewModel: DetailViewModel by viewModels()

    private val pokemonTypeAdapter by lazy { PokemonTypeAdapter() }
    private val pokemonImageAdapter by lazy { PokemonImageAdapter() }
    private val pokemonShinyImageAdapter by lazy { PokemonImageAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@DetailActivity
            vm = viewModel
        }
        setContentView(binding.root)

        setupUI()
        observeViewModel()

        getPokemonFromIntent()?.let { pokemon ->
            binding.pokemon = pokemon
            viewModel.fetchPokemonDetail(pokemon.name)
            updatePokemonImages(pokemon)
        } ?: viewModel.setToastMessage(getString(R.string.data_missing_message))
    }

    private fun setupUI() {
        setupRecyclerViews()
    }

    private fun getPokemonFromIntent(): Pokemon? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_POKEMON, Pokemon::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_POKEMON)
        }

    private fun setupRecyclerViews() {
        setupRecyclerView(binding.imageRecyclerView, pokemonImageAdapter, GridLayoutManager(this, 2))
        setupRecyclerView(binding.shinyImageRecyclerView, pokemonShinyImageAdapter, GridLayoutManager(this, 2))
        setupRecyclerView(binding.typeRecyclerView, pokemonTypeAdapter, LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, layoutManager: RecyclerView.LayoutManager) {
        recyclerView.run {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
    }

    private fun updatePokemonImages(pokemon: Pokemon) {
        pokemonImageAdapter.setItems(listOf(pokemon.getGifUrl(), pokemon.getBackGifUrl()))
        pokemonShinyImageAdapter.setItems(listOf(pokemon.getShinyGifUrl(), pokemon.getShinyBackGifUrl()))
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.pokemonDetail.collectLatest { pokemonDetail ->
                binding.pokemonDetail = pokemonDetail
                pokemonDetail?.types?.map { it.type }?.let(pokemonTypeAdapter::setItems)
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

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_POKEMON = "EXTRA_POKEMON"
    }
}