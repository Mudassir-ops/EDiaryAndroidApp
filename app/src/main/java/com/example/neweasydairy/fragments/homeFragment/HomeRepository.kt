package com.example.neweasydairy.fragments.homeFragment

import com.example.neweasydairy.data.NotePadDao
import com.example.neweasydairy.data.NotepadEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class HomeRepository @Inject constructor(private val notePadDao: NotePadDao) {

    fun getAllNotes(): Flow<List<NotepadEntity>> {
        return notePadDao.getAllNotes()
    }

    suspend fun getNotesByDate(startOfDay: Long, endOfDay: Long): List<NotepadEntity> {
        return notePadDao.getNotesForDate(startOfDay, endOfDay)
    }
    suspend fun deleteNoteById(noteId: Int) {
        notePadDao.deleteNoteById(noteId)
    }
}