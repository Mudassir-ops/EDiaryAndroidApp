package com.example.neweasydairy.fragments.tags

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
    private var list: List<CustomTagEntity>,
    private val context: Context,
    private val onItemClick : (Pair<CustomTagEntity,Int>)-> Unit
    ):RecyclerView.Adapter<TagsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTagsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = list[position]
        holder.binding.txtTagName.text = dataModel.tagName
        holder.binding.icEdit.setOnClickListener {
            onItemClick.invoke(Pair(dataModel,EDIT_ACTION))
        }
        holder.binding.icDelete.setOnClickListener {
            onItemClick.invoke(Pair(dataModel, DELETE_ACTION))
        }
        holder.itemView.setOnClickListener {
            onItemClick.invoke(Pair(dataModel, ITEM_CLICK))
        }
    }

    class ViewHolder(val binding:ItemTagsBinding):RecyclerView.ViewHolder(binding.root)


    fun updateTagList(newTagList: List<CustomTagEntity>) {
        list = newTagList
        notifyDataSetChanged()
    }
}



