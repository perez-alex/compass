package com.example.compass.about.adapter

import com.example.compass.databinding.ItemCharacterBinding

class CharacterViewHolder(
    private val binding: ItemCharacterBinding
) : CharactersAdapter.BaseViewHolder<String>(binding.root) {

    override fun bind(item: String) {
        binding.apply {
            root.text = item
        }
    }
}