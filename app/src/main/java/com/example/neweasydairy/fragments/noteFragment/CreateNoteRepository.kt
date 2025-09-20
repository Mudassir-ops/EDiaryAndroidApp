package com.example.neweasydairy.fragments.noteFragment

import com.example.neweasydairy.data.CustomTagEntity
import com.example.neweasydairy.data.NotePadDao
import com.example.neweasydairy.data.NotepadEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    fun getAllTagsFromNotes(): Flow<List<CustomTagEntity>> {
        return notePadDao.getAllNotesTags().map {
            it.flatMap { tags -> tags.tagsList ?: emptyList() }
        }
    }

    suspend fun updateImagesList(noteId: Int, imagePath: String) {
        val note = notePadDao.getNoteById(noteId)
        val updatedList = note?.imageList?.filter { it.imagePath != imagePath } ?: listOf()
        val updatedNote = note?.copy(imageList = updatedList)
        updatedNote?.let { notePadDao.update(note = it) }
    }
}