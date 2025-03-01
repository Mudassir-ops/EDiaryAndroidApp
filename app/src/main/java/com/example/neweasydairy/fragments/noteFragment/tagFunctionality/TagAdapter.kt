package com.example.neweasydairy.fragments.noteFragment.tagFunctionality

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.databinding.ItemCreateTagListBinding

class TagAdapter(
    private var tagList: MutableList<String>,
    private val context: Context,
    private val onTagClick: (String) -> Unit,
    private val onCloseClick: (String) -> Unit
) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCreateTagListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = tagList[position]
        holder.binding.txtTagListText.text = tag

        holder.binding.root.setOnClickListener {
            onTagClick(tag)
        }

        holder.binding.imgClose.setOnClickListener {
            onCloseClick(tag)
            tagList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTagList(newList: List<String>) {
        tagList.clear()
        tagList.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemCreateTagListBinding) : RecyclerView.ViewHolder(binding.root)
}
