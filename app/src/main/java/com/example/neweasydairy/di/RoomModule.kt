package com.example.neweasydairy.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.neweasydairy.data.CustomTagDao
import com.example.neweasydairy.data.NotePadDao
import com.example.neweasydairy.data.NotepadDatabase
import com.example.neweasydairy.data.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideNotepadDatabase(@ApplicationContext context: Context): NotepadDatabase {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE NotePad ADD COLUMN newField TEXT")
            }
        }

        return Room.databaseBuilder(
            context.applicationContext,
            NotepadDatabase::class.java,
            "note_pad_database"
        ).addMigrations(MIGRATION_1_2).build()
    }
    @Provides
    fun provideNoteDao(database: NotepadDatabase): NotePadDao {
        return database.notepadDao()
    }
    @Provides
    fun provideReminderDao(database: NotepadDatabase): ReminderDao {
        return database.reminderDao()
    }

    @Provides
    fun provideCustomTagDao(database: NotepadDatabase): CustomTagDao {
        return database.customTagDao()
    }
}
