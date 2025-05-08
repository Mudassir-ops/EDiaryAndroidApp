package com.example.neweasydairy.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "reminder_table")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val reminderDate: String,
    val reminderTime: String,
    val description: String? = null,
    val scheduleAt: Long
) : Parcelable