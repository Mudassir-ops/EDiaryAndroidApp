package com.example.neweasydairy.fragments.libraryFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.data.NotepadEntity

class ImageListAdapter(private val images: List<String>,
                       private val onImageClick: (String) -> Unit) :
    RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item_new, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = images[position]
        Glide.with(holder.imageView.context)
            .load(imagePath)
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            onImageClick(imagePath)
        }
    }

    override fun getItemCount(): Int = images.size
}
