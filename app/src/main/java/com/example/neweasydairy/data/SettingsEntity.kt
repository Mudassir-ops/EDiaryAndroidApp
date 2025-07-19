package com.example.neweasydairy.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "settings_table")
data class SettingsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val sortingOrder: Boolean = false
)
