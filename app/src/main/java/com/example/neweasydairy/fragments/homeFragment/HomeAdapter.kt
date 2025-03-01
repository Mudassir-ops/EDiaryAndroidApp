package com.example.neweasydairy.fragments.homeFragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.HomeItemNewBinding
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.utilis.formatDate

class HomeAdapter(
    var list: List<NotepadEntity>,
    private val onItemClick: (NotepadEntity) -> Unit,
    private val onItemLongClick: (NotepadEntity) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private val context:Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeItemNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = list[position]
        holder.itemView.setOnClickListener {
            onItemClick(dataModel)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(dataModel)
            true
        }
        val date = context.formatDate(dataModel.timestamp)
        val cleanedTagsText = dataModel.tagsText.replace("[", "").replace("]", "")
        val tags = cleanedTagsText.split(",").map { it.trim() }

        holder.binding.apply {
            txtTimeAndDate.text = date
            if (tags.isNotEmpty() && tags[0].isNotBlank()) {
                txtTag1.text = tags[0]
                txtTag1.visibility = View.VISIBLE
            } else {
                txtTag1.visibility = View.GONE
            }

            if (tags.size > 1 && tags[1].isNotBlank()) {
                txtTag2.text = tags[1]
                txtTag2.visibility = View.VISIBLE
            } else {
                txtTag2.visibility = View.GONE
            }

            if (dataModel.imageList.isEmpty()) {
                icImage.visibility = View.GONE
            } else {
                icImage.visibility = View.VISIBLE
            }
            when (dataModel.icEmojiName) {
                "One" -> topView.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_one)
                "Two" -> topView.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_two)
                "Three" -> topView.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_three)
                "Four" -> topView.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_four)
                "Five" -> topView.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_five)
                "Six" -> topView.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_six)
                else -> topView.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_one)
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<NotepadEntity>) {
        list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: HomeItemNewBinding) : RecyclerView.ViewHolder(binding.root)
}