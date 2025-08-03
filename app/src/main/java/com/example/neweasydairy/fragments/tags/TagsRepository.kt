package com.example.neweasydairy.fragments.tags

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.neweasydairy.data.CustomTagEntity
import com.example.neweasydairy.data.NotePadDao
import com.example.neweasydairy.fragments.noteFragment.CreateNoteRepository
import com.google.gson.Gson
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

class TagsRepository @Inject constructor(
    private val createNoteRepository: CreateNoteRepository,
    private val notePadDao: NotePadDao
) {

    private val _localTags = mutableListOf<String>()
    private val _localTagsFlow = MutableSharedFlow<List<String>>(replay = 1)
    val localTagsFlow: SharedFlow<List<String>> = _localTagsFlow

    private val _allTagsFlow = MutableStateFlow<List<CustomTagEntity>>(listOf())
    val allTagsFlow: SharedFlow<List<CustomTagEntity>> = _allTagsFlow

    suspend fun insertLocalTag(tag: String) {
        if (_localTags.contains(tag)) return
        Log.e("updatedText", "onCreate: $tag")
        _localTags.add(tag)
        _localTagsFlow.emit(_localTags.toList())
    }

    suspend fun addAllTagsForCreatedNote(allTags: List<String>) {
        _localTags.clear()
        _localTags.addAll(allTags)
        _localTagsFlow.emit(_localTags.toList())
    }

    suspend fun removeTag(tag: String) {
        _localTags.remove(tag)
        _localTagsFlow.emit(_localTags.toList())
    }

    suspend fun clearLocalTags() {
        _localTags.clear()
        _localTagsFlow.emit(_localTags)
    }

    suspend fun getAllTags(lifecycle: Lifecycle) {
        createNoteRepository.getAllTagsFromNotes().flowWithLifecycle(lifecycle).collect {
            _allTagsFlow.emit(it)
        }
    }

    suspend fun updateTag(noteId: Int, oldTag: CustomTagEntity?, newTag: CustomTagEntity?) {
        val gson = Gson()

        val existingNote = notePadDao.getNoteById(noteId)
        val existingTags: MutableList<CustomTagEntity> =
            (existingNote?.tagsList ?: emptyList()).toMutableList()

        when {
            //  Delete
            oldTag != null && newTag != null && oldTag == newTag -> {
                existingTags.removeIf { it == oldTag }
            }

            //  Edit
            oldTag != null && newTag != null -> {
                val index = existingTags.indexOfFirst { it == oldTag }
                if (index != -1) {
                    existingTags[index] = newTag
                } else {
                    existingTags.add(newTag)
                }
            }



            // No change
            else -> return
        }

        notePadDao.updateTag(noteId, gson.toJson(existingTags))
    }


}


