package com.example.neweasydairy.fragments.noteFragment.imageFunctionality

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.ImageItemBinding

class ImageAdapter(
  var imageList: List<ImageDataModelGallery>,
    val context: Context,
    private val onImageClick: (Pair<ImageDataModelGallery, Int>) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val dataModel = imageList[position]
        holder.itemView.setOnClickListener {
            onImageClick(Pair(dataModel, position))
        }
        Glide.with(holder.binding.notesImageView.context).load(dataModel.imagePath)
            .placeholder(R.drawable.ic_image).error(R.drawable.ic_six)
            .into(holder.binding.notesImageView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateImageList(newList:List<ImageDataModelGallery>){
        imageList = newList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        imageList = emptyList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root)
}