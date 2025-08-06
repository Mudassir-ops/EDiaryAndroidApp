package com.example.neweasydairy.fragments.homeFragment

import android.content.SharedPreferences
import com.example.neweasydairy.data.NotePadDao
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.utilis.BooleanObjects.languageDoneButton
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class HomeRepository @Inject constructor(
    private val notePadDao: NotePadDao,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getNotesByDate(startOfDay: Long, endOfDay: Long): List<NotepadEntity> {
        return notePadDao.getNotesForDate(startOfDay, endOfDay)
    }

    suspend fun deleteNoteById(noteId: Int) {
        notePadDao.deleteNoteById(noteId)
    }

    fun isRatingDialogShown(): Boolean {
        return sharedPreferences.getBoolean("rating_dialog_shown", false)
    }

    fun setRatingDialogShown() {
        sharedPreferences.edit().putBoolean("rating_dialog_shown", true).apply()
    }


}