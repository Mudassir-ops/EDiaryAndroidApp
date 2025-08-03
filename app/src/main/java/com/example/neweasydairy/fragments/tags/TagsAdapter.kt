package com.example.neweasydairy.fragments.tags

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.databinding.ItemTagsBinding
import com.example.neweasydairy.data.CustomTagEntity
import com.example.neweasydairy.utilis.Objects.DELETE_ACTION
import com.example.neweasydairy.utilis.Objects.EDIT_ACTION
import com.example.neweasydairy.utilis.Objects.ITEM_CLICK

class TagsAdapter(
    private val context: Context,
    private val onItemClick: (Pair<CustomTagEntity, Int>) -> Unit
) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    private var fullList: List<CustomTagEntity> = emptyList()
    private var filteredList: List<CustomTagEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTagsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = filteredList[position]
        holder.binding.txtTagName.text = dataModel.tagName

        holder.binding.icEdit.setOnClickListener {
            onItemClick.invoke(Pair(dataModel, EDIT_ACTION))
        }
        holder.binding.icDelete.setOnClickListener {
            onItemClick.invoke(Pair(dataModel, DELETE_ACTION))
        }
        holder.itemView.setOnClickListener {
            onItemClick.invoke(Pair(dataModel, ITEM_CLICK))
        }
    }

    class ViewHolder(val binding: ItemTagsBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setTags(tags: List<CustomTagEntity>) {
        fullList = tags
        filteredList = tags
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.tagName.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}




