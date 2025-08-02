package com.example.neweasydairy.fragments.noteFragment

import com.example.neweasydairy.data.NotePadDao
import com.example.neweasydairy.data.NotepadEntity
import jakarta.inject.Inject

class CreateNoteRepository @Inject constructor(private val notePadDao: NotePadDao) {
    suspend fun insertNoteData(noteEntity: NotepadEntity): Long {
        return notePadDao.insertNoteData(noteEntity)
    }

    suspend fun updateNoteData(noteEntity: NotepadEntity) {
        notePadDao.update(noteEntity)
    }

    suspend fun updateTag(noteId: Int, tagsList: String) {
        notePadDao.updateTag(noteId = noteId, tagsList = tagsList)
    }

}