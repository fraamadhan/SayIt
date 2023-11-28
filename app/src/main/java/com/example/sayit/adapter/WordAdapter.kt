package com.example.sayit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sayit.databinding.ItemWordsBinding
import com.example.sayit.model.WordItem

class WordAdapter: ListAdapter<WordItem, WordAdapter.MyViewHolder> (DIFF_CALLBACK){
    class MyViewHolder(private val binding: ItemWordsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(word: WordItem) {
            binding.wordItem.text = word.word
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val word = getItem(position)
        if (word != null) {
            holder.bind(word)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemWordsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WordItem>(){
            override fun areItemsTheSame(
                oldItem: WordItem,
                newItem: WordItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: WordItem,
                newItem: WordItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}