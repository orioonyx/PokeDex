package com.github.orioonyx.pokedex.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.orioonyx.pokedex.domain.model.Pokemon
import com.github.orioonyx.pokedex.ui.main.MainViewModel
import com.github.orioonyx.pokedex.ui.main.PokemonAdapter
import com.github.orioonyx.pokedex.utils.RecyclerViewPagination

object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindRecyclerViewAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        view.adapter = adapter.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    @BindingAdapter("pokemonList")
    fun bindPokemonList(view: RecyclerView, pokemonList: List<Pokemon>?) {
        (view.adapter as? PokemonAdapter)?.submitList(pokemonList)
    }

    @JvmStatic
    @BindingAdapter("paginationPokemonList")
    fun bindPagination(view: RecyclerView, viewModel: MainViewModel) {
        RecyclerViewPagination(
            recyclerView = view,
            isLoading = { viewModel.isLoading.value ?: false },
            loadMore = { viewModel.fetchNextPokemonList() },
            onLast = { false }
        ).run {
            threshold = 8
        }
    }
}

