package com.joel.wordapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joel.wordapp.databinding.ItemWordLayoutBinding
import com.joel.wordapp.data.models.Word

// WordAdapter class, contains functions and information for the RecyclerView(list that holds all the words)
// -> items (list of the words), onClick (function to get details of a specific word to navigate to that word's details)
class WordAdapter(private var items: List<Word>, val onClick: (item: Word) -> Unit) :
    RecyclerView.Adapter<WordAdapter.ItemWordHolder>() {

    // create the UI that will hold and display the data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemWordHolder {
        val binding =
            ItemWordLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemWordHolder(binding)
    }

    // binds the Words data that will be displayed by the above created UI
    override fun onBindViewHolder(holder: ItemWordHolder, position: Int) {
        val item = items[position]
        holder.binding.run {
            tvTitle.text = item.title
            tvMeaning.text = item.meaning
            cvWord.setOnClickListener {
                onClick(item)
            }
        }
    }

    // function that returns the number of words data in the list
    override fun getItemCount() = items.size

    // fetches the list of words and "refreshes" the cache if there are any changes to the list
    fun setWords(items: List<Word>) {
        this.items = items
        notifyDataSetChanged()
    }

    // a child class that is binding to the UI, and being used by the above class
    class ItemWordHolder(val binding: ItemWordLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}