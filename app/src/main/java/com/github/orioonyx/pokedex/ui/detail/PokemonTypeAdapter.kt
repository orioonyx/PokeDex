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
import com.github.orioonyx.pokedex.databinding.ItemPokemonTypeBinding
import com.github.orioonyx.pokedex.domain.model.PokemonDetail

class PokemonTypeAdapter : RecyclerView.Adapter<PokemonTypeAdapter.TypeViewHolder>() {

    private val typeList = mutableListOf<PokemonDetail.TypeInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = ItemPokemonTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.bind(typeList[position])
    }

    override fun getItemCount(): Int = typeList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(types: List<PokemonDetail.TypeInfo>) {
        typeList.clear()
        typeList.addAll(types)
        notifyDataSetChanged()
    }

    inner class TypeViewHolder(private val binding: ItemPokemonTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(type: PokemonDetail.TypeInfo) {
            binding.type = type
            binding.executePendingBindings()
        }
    }
}
