package com.example.neweasydairy.fragments.languageFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.LanguageItemBinding

class LanguageAdapter(
    private val context: Context,
    private var languageList: List<LanguageDataModel>,
    private val onLanguageSelected: (Int) -> Unit
): RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LanguageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return languageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = languageList[position]
        holder.binding.icCountryFlag.setImageResource(dataModel.countryFlag)
        holder.binding.languageName.text = dataModel.languageName

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_selected)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_unselected)
        }
        holder.itemView.setOnClickListener {
            if (selectedPosition != position) {
                val previousSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedPosition)
                onLanguageSelected(position)
            }
        }
    }
    class ViewHolder(val binding: LanguageItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateLanguageList(newLanguageList: List<LanguageDataModel>) {
        languageList = newLanguageList
        notifyDataSetChanged()
    }
}