package com.example.compass.about.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.compass.databinding.ItemCharacterBinding

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.BaseViewHolder<*>>() {

    private var list = ArrayList<String>()

    companion object {
        private const val TYPE_CHAR = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_CHAR -> CharacterViewHolder(
                ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = list[position]
        when (holder) {
            is CharacterViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CHAR
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(items: List<String>, clearList: Boolean = false) {
        if (clearList) {
            this.list.clear()
            this.list.addAll(items)
            notifyDataSetChanged()
        } else {
            val firstNewPosition = this.list.size
            this.list.addAll(items)
            notifyItemRangeInserted(firstNewPosition, items.size)
        }
    }
}