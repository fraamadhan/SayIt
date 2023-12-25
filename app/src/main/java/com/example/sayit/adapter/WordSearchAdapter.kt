package com.example.sayit.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sayit.databinding.ItemWordsBinding
import com.example.sayit.model.WordItem
import com.example.sayit.utils.WORD_ID
import com.example.sayit.view.detailword.DetailWordActivity

class WordSearchAdapter: ListAdapter<WordItem, WordSearchAdapter.MyViewHolder> (DIFF_CALLBACK){
    class MyViewHolder(private val binding: ItemWordsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(word: WordItem) {
            binding.wordItem.text = word.word
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val word = getItem(position)
        if (word != null) {
            holder.bind(word)
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailWordActivity::class.java)
                intent.putExtra(WORD_ID, word.id.toInt())
                holder.itemView.context.startActivity(intent)
            }
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