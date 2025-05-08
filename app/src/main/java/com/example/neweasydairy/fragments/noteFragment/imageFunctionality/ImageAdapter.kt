package com.example.neweasydairy.fragments.noteFragment.imageFunctionality

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.ImageItemBinding
import java.io.File

class ImageAdapter(
  var imageList: MutableList<ImageDataModelGallery>,
    val context: Context,
  private val onShareClick: (String) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val dataModel = imageList[position]
        Glide.with(holder.binding.notesImageView.context).load(dataModel.imagePath)
            .placeholder(R.drawable.ic_image).error(R.drawable.ic_six)
            .into(holder.binding.notesImageView)

        holder.binding.icSetting.setOnClickListener {
            val popup = PopupMenu(context, holder.binding.icSetting, Gravity.END)
            popup.menuInflater.inflate(R.menu.image_item_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_share -> {
                        val filePath = dataModel.imagePath
                        val cleanFilePath = if (filePath.startsWith("file://")) {
                            filePath.removePrefix("file://")
                        } else {
                            filePath
                        }
                        val file = File(cleanFilePath)
                        try {
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                file
                            )

                            Log.d("ImageAdapter", "URI: $uri")

                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "image/*"
                            intent.putExtra(Intent.EXTRA_STREAM, uri)
                            context.startActivity(Intent.createChooser(intent, "Share Image"))
                        } catch (e: Exception) {
                            Log.e("ImageAdapter", "Error in sharing image", e)
                        }

                        true
                    }
                    R.id.menu_delete -> {
                        imageList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, imageList.size)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateImageList(newList:MutableList<ImageDataModelGallery>){
        imageList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root)
}
