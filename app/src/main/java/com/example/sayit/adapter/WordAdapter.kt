package com.example.sayit.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sayit.databinding.ItemWordsBinding
import com.example.sayit.model.WordItem

class WordAdapter: ListAdapter<WordItem, WordAdapter.MyViewHolder> (DIFF_CALLBACK), Filterable{

    private var originalList: List<WordItem>? = null

    fun setData(data: List<WordItem>) {
        submitList(data)
        originalList = data.toList()
    }
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

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {

                val filterResults = FilterResults()

                if (originalList == null) {
                    originalList = currentList.toList()
                }

                if (charSequence.isNullOrEmpty()) {
                    filterResults.values = originalList
                    filterResults.count = originalList?.size ?: 0
                } else {
                    val filteredList = originalList?.filter {
                        it.word?.contains(charSequence, ignoreCase = true) == true
                    }

                    filterResults.values = filteredList
                    Log.d("FILTER", filterResults.values.toString())
                    filterResults.count = filteredList?.size ?: 0
                }

                return filterResults

            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults?) {
                submitList(filterResults?.values as? List<WordItem>)
            }

        }
    }

}