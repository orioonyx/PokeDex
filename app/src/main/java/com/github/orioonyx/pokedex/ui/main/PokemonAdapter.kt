/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.orioonyx.pokedex.R
import com.github.orioonyx.pokedex.databinding.ItemPokemonBinding
import com.github.orioonyx.pokedex.domain.model.Pokemon

class PokemonAdapter(private val clickHandler: (Pokemon) -> Unit) :
    ListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonDiffCallback()) {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding, clickHandler)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            viewToAnimate.startAnimation(
                AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.item_animation_slide_in)
            )
            lastPosition = position
        }
    }

    override fun onViewDetachedFromWindow(holder: PokemonViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun submitList(list: List<Pokemon>?) {
        super.submitList(list)
        lastPosition = -1
    }

    inner class PokemonViewHolder(
        private val binding: ItemPokemonBinding,
        private val clickHandler: (Pokemon) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: Pokemon) {
            binding.pokemon = pokemon
            binding.root.setOnClickListener { clickHandler(pokemon) }
            binding.executePendingBindings()
        }
    }
}

class PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem == newItem
}
