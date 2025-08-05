package com.example.neweasydairy.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.neweasydairy.typeConverter.ImageConverter

@Database(
    entities =
    [NotepadEntity::class, CustomTagEntity::class, ReminderEntity::class, SettingsEntity::class],
    version = 10, exportSchema = false
)
@TypeConverters(ImageConverter::class, CustomTagListConverter::class)
abstract class NotepadDatabase : RoomDatabase() {
    abstract fun notepadDao(): NotePadDao
    abstract fun customTagDao(): CustomTagDao
    abstract fun reminderDao(): ReminderDao
    abstract fun settingsDao(): SettingsDao
}