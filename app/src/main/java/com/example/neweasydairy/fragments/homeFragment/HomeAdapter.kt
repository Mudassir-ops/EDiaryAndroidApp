package com.example.neweasydairy.fragments.homeFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.HomeItemNewBinding
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.utilis.formatDate
import com.example.neweasydairy.utilis.formatTimestampForDisplay

class HomeAdapter(
    var list: List<NotepadEntity>,
    private val onItemClick: (NotepadEntity) -> Unit,
    private val onItemLongClick: (NotepadEntity) -> Unit
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

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

        val cleanedTagsText = (dataModel.tagsText.takeIf { it.isNotBlank() } ?: "unknown")
            .replace("[", "")
            .replace("]", "")

        val tags = cleanedTagsText.split(",").map { it.trim() }


        holder.binding.apply {
            txtTimeAndDate.text = dataModel.timestamp.formatTimestampForDisplay()
            Log.e("tagsHere-->", "onBindViewHolder: $tags")
            if (tags.isNotEmpty() && tags[0].isNotBlank()) {
                txtTag1.text = tags[0]
                txtTag1.visibility = View.VISIBLE
            } else {
                txtTag1.text = txtTag1.context.getString(R.string.personal)
                txtTag1.visibility = View.VISIBLE
            }
            if (dataModel.imageList.isEmpty()) {
                icImage.visibility = View.INVISIBLE
            } else {
                icImage.visibility = View.VISIBLE
            }
            when (dataModel.icEmojiName) {
                "One" -> {
                    topView.background =
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_one)
                    txtTag2.text = txtTag2.context.getString(R.string.happy)
                    txtTag2.adjustModDrawableShape(color = "#D07B80")
                    modeStartDrawable.setModDrawAble(res = R.drawable.happy)
                }

                "Two" -> {
                    topView.background =
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_two)
                    txtTag2.text = txtTag2.context.getString(R.string.angry)
                    txtTag2.adjustModDrawableShape(color = "#C97B52")
                    modeStartDrawable.setModDrawAble(res = R.drawable.angry)
                }

                "Three" -> {
                    topView.background =
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_three)
                    txtTag2.text = txtTag2.context.getString(R.string.calm)
                    txtTag2.adjustModDrawableShape(color = "#61A3C0")
                    modeStartDrawable.setModDrawAble(res = R.drawable.happy)
                }

                "Four" -> {
                    topView.background =
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_four)
                    txtTag2.text = txtTag2.context.getString(R.string.cheeky)
                    txtTag2.adjustModDrawableShape(color = "#635FBF")
                    modeStartDrawable.setModDrawAble(res = R.drawable.happy)
                }

                "Five" -> {
                    topView.background =
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_five)
                    txtTag2.text = txtTag2.context.getString(R.string.excited)
                    txtTag2.adjustModDrawableShape(color = "#AC9158")
                    modeStartDrawable.setModDrawAble(res = R.drawable.happy)
                }

                "Six" -> {
                    topView.background =
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_six)
                    txtTag2.text = txtTag2.context.getString(R.string.meh)
                    txtTag2.adjustModDrawableShape(color = "#59C39A")
                    modeStartDrawable.setModDrawAble(res = R.drawable.happy)
                }

                else -> {
                    topView.background =
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_item_one)
                    txtTag2.text = txtTag2.context.getString(R.string.happy)
                    txtTag2.adjustModDrawableShape(color = "#D07B80")
                    modeStartDrawable.setModDrawAble(res = R.drawable.happy)
                }
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<NotepadEntity>) {
        list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: HomeItemNewBinding) : RecyclerView.ViewHolder(binding.root)

    private fun AppCompatTextView.adjustModDrawableShape(color: String) {
        val drawable =
            ContextCompat.getDrawable(this.context, R.drawable.bg_tag) as? GradientDrawable
        drawable?.setStroke(2, Color.parseColor(color))
        this.background = drawable
    }

    private fun AppCompatImageView.setModDrawAble(res: Int) {
        this.setImageResource(res)
    }

}