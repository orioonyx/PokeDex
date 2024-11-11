/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.ui.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.orioonyx.pokedex.databinding.ItemPokemonImageBinding

class PokemonImageAdapter : RecyclerView.Adapter<PokemonImageAdapter.PokemonGifViewHolder>() {

    private val urlList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonGifViewHolder {
        val binding = ItemPokemonImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonGifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonGifViewHolder, position: Int) {
        holder.bind(urlList[position])
    }

    override fun getItemCount(): Int = urlList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(urls: List<String>) {
        urlList.apply {
            clear()
            addAll(urls)
        }
        notifyDataSetChanged()
    }

    inner class PokemonGifViewHolder(private val binding: ItemPokemonImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) {
            binding.url = url
            binding.executePendingBindings()
        }
    }
}
