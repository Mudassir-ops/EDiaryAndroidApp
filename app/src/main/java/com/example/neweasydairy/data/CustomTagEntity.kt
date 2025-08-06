package com.example.neweasydairy.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "custom_tag_table")
data class CustomTagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tagName: String,
    val noteId: Int? = null
) : Parcelable
