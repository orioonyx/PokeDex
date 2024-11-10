package com.github.orioonyx.pokedex.ui.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.orioonyx.pokedex.databinding.ActivityDetailBinding
import com.github.orioonyx.pokedex.domain.model.Pokemon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var binding: ActivityDetailBinding

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val viewModel: DetailViewModel by viewModels()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val pokemonTypeAdapter by lazy { PokemonTypeAdapter() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val pokemonImageAdapter by lazy { PokemonImageAdapter() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val pokemonShinyImageAdapter by lazy { PokemonImageAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.vm = viewModel

        setupRecyclerViews()
        observeViewModel()

        val pokemon = getPokemonFromIntent()
        pokemon?.let {
            binding.pokemon = it
            viewModel.fetchPokemonDetail(it.name)
            setupPokemonImages(it)
        }
    }

    private fun getPokemonFromIntent(): Pokemon? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_POKEMON, Pokemon::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_POKEMON)
        }
    }

    private fun setupRecyclerViews() {
        setupImageRecyclerView()
        setupShinyImageRecyclerView()
        setupTypeRecyclerView()
    }

    private fun setupImageRecyclerView() {
        binding.imageRecyclerView.apply {
            adapter = pokemonImageAdapter
            layoutManager = GridLayoutManager(this@DetailActivity, 2)
        }
    }

    private fun setupShinyImageRecyclerView() {
        binding.shinyImageRecyclerView.apply {
            adapter = pokemonShinyImageAdapter
            layoutManager = GridLayoutManager(this@DetailActivity, 2)
        }
    }

    private fun setupTypeRecyclerView() {
        binding.typeRecyclerView.apply {
            adapter = pokemonTypeAdapter
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupPokemonImages(pokemon: Pokemon) {
        pokemonImageAdapter.setItems(
            listOf(pokemon.getGifUrl(), pokemon.getBackGifUrl())
        )
        pokemonShinyImageAdapter.setItems(
            listOf(pokemon.getShinyGifUrl(), pokemon.getShinyBackGifUrl())
        )
    }

    private fun observeViewModel() {
        viewModel.pokemonDetail.observe(this) { pokemonDetail ->
            binding.pokemonDetail = pokemonDetail
            pokemonDetail?.types?.let { pokemonTypeAdapter.setItems(it.map { it -> it.type }) }
        }
    }

    companion object {
        const val EXTRA_POKEMON = "EXTRA_POKEMON"
    }
}
