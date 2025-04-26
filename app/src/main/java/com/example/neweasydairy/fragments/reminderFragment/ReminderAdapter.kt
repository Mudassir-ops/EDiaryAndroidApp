package com.example.neweasydairy.fragments.reminderFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.databinding.ItemReminderBinding
import com.example.neweasydairy.data.ReminderEntity
import com.example.neweasydairy.utilis.Objects.DELETE_ACTION_REMINDER

class ReminderAdapter(
    private var list: List<ReminderEntity>,
    private val context: Context,
    private val onItemClick : (ReminderEntity)-> Unit
): RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = list[position]
        holder.binding.txtReminderTime.text = dataModel.reminderTime
        holder.binding.icCross.setOnClickListener {
            onItemClick.invoke(dataModel)
        }

    }

    class ViewHolder(val binding: ItemReminderBinding):RecyclerView.ViewHolder(binding.root)

    fun updateReminderList(newReminderList: List<ReminderEntity>) {
        list = newReminderList
        notifyDataSetChanged()
    }

    fun removeItem(reminder: ReminderEntity) {
        val updatedList = list.toMutableList()
        updatedList.remove(reminder)
        list = updatedList
        notifyDataSetChanged()
    }

}
