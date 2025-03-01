package com.example.neweasydairy.fragments.profileFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.databinding.DrawerItemBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageAdapter.ViewHolder

class ProfileAdapter(
    private val context: Context,
    private var profileList: List<ProfileDataModel>,
    private val onItemSelected: (Int) -> Unit


):RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    private var selectedPosition = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrawerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = profileList[position]
        holder.binding.icSetting.setImageResource(dataModel.icon)
        holder.binding.txtTitle.text = dataModel.title

        holder.itemView.setOnClickListener {
            onItemSelected(position)
//            if (selectedPosition != position) {
//                val previousSelectedPosition = selectedPosition
//                selectedPosition = position
//                notifyItemChanged(previousSelectedPosition)
//                notifyItemChanged(selectedPosition)
//                onItemSelected(position)
//            }
        }
    }

    override fun getItemCount(): Int {
        return profileList.size
    }
    class ViewHolder(val binding:DrawerItemBinding):RecyclerView.ViewHolder(binding.root)

    fun updateProfileList(newProfileList: List<ProfileDataModel>) {
        profileList = newProfileList
        notifyDataSetChanged()
    }
}