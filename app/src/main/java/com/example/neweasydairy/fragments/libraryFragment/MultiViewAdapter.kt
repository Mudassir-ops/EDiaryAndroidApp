package com.example.neweasydairy.fragments.libraryFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.R

class MultiViewAdapter(
    private val items: List<LibraryItem>,
    private val onImageClick: (String, String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DATE = 0
        private const val TYPE_IMAGES = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is LibraryItem.DateItem -> TYPE_DATE
            is LibraryItem.ImagesItem -> TYPE_IMAGES
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DATE -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_date_header, parent, false)
                DateViewHolder(view)
            }

            TYPE_IMAGES -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
                ImagesViewHolder(view, onImageClick)
            }

            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is LibraryItem.DateItem -> (holder as DateViewHolder).bind(item)
            is LibraryItem.ImagesItem -> (holder as ImagesViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: LibraryItem.DateItem) {
            itemView.findViewById<TextView>(R.id.tvDate).text = item.date
        }
    }

    class ImagesViewHolder(
        view: View,
        private val onImageClick: (String, String) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val rvImages = view.findViewById<RecyclerView>(R.id.rvImages)

        fun bind(item: LibraryItem.ImagesItem) {
            rvImages.layoutManager =
                LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            rvImages.adapter = HorizontalImageAdapter(item.imagePaths, item.date, onImageClick)
        }
    }
}
