package com.example.neweasydairy.fragments.noteFragment

import com.example.neweasydairy.data.NotePadDao
import com.example.neweasydairy.data.NotepadEntity
import jakarta.inject.Inject

class CreateNoteRepository @Inject constructor(private val notePadDao: NotePadDao) {
    suspend fun insertNoteData(noteEntity: NotepadEntity) {
        notePadDao.insertNoteData(noteEntity)
    }
    suspend fun updateNoteData(noteEntity: NotepadEntity) {
        notePadDao.update(noteEntity)
    }
}