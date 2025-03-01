package com.example.neweasydairy.fragments.colorTheme

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.databinding.ItemColorThemeBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageDataModel

class ColorThemeAdapter(
    private val context: Context,
    private var colorThemeList: List<ColorThemeDataModel>,
    private val onColorThemeSelected: (Int) -> Unit
) :
    RecyclerView.Adapter<ColorThemeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemColorThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = colorThemeList[position]
        holder.binding.imgColorTheme.setImageResource(dataModel.colorThemeImage)
        holder.itemView.setOnClickListener {
            onColorThemeSelected.invoke(position)
        }


    }

    override fun getItemCount(): Int {
        return colorThemeList.size
    }

    class ViewHolder(val binding: ItemColorThemeBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateColorThemeList(newColorThemeList: List<ColorThemeDataModel>) {
        colorThemeList = newColorThemeList
        notifyDataSetChanged()
    }
}