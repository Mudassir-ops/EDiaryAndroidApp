package com.example.neweasydairy.fragments.libraryFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.data.NotepadEntity

class LibraryAdapter(private val notes: List<NotepadEntity>) :
    RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val rvImages: RecyclerView = itemView.findViewById(R.id.rvImages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.library_item, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val note = notes[position]

        val images = note.imageList.map { it.imagePath }

        if (images.isEmpty()) {
            holder.tvDate.visibility = View.GONE
        } else {
            holder.tvDate.visibility = View.VISIBLE
            holder.tvDate.text = note.timestamp.toDateString()
        }

       // holder.tvDate.text = note.timestamp.toDateString()
        holder.rvImages.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.rvImages.adapter = ImageListAdapter(images)
    }

    override fun getItemCount(): Int = notes.size
}

fun Long.toDateString(): String {
    val sdf = java.text.SimpleDateFormat("d MMMM, yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(this))
}
