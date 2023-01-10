package com.joel.wordapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joel.wordapp.databinding.ItemWordLayoutBinding
import com.joel.wordapp.data.models.Word

class WordAdapter(private var items: List<Word>, val onClick: (item: Word) -> Unit) :
    RecyclerView.Adapter<WordAdapter.ItemWordHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemWordHolder {
        val binding =
            ItemWordLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemWordHolder(binding)
    }

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

    override fun getItemCount() = items.size

    fun setWords(items: List<Word>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ItemWordHolder(val binding: ItemWordLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}