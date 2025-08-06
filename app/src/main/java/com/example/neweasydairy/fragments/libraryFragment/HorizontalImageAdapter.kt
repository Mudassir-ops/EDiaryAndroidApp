package com.example.neweasydairy.fragments.libraryFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easydiaryandjournalwithlock.R

class HorizontalImageAdapter(
    private val imagePaths: List<String>,
    private val date: String,
    private val onImageClick: (String, String) -> Unit
) : RecyclerView.Adapter<HorizontalImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_single_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val path = imagePaths[position]
        holder.bind(path)
        holder.itemView.setOnClickListener { onImageClick(path, date) }
    }

    override fun getItemCount(): Int = imagePaths.size

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        fun bind(imagePath: String) {
            Glide.with(imageView.context)
                .load(imagePath)
                .into(imageView)
        }
    }
}
