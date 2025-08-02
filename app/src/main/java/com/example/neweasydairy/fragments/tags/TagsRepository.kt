package com.example.neweasydairy.fragments.tags

import android.util.Log
import com.example.neweasydairy.data.CustomTagDao
import com.example.neweasydairy.data.CustomTagEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class TagsRepository @Inject constructor(private val customTagDao: CustomTagDao) {

    private val _localTags = mutableListOf<String>()
    private val _localTagsFlow = MutableSharedFlow<List<String>>(replay = 1)
    val localTagsFlow: SharedFlow<List<String>> = _localTagsFlow

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

    suspend fun clearLocalTags() {
        _localTags.clear()
        _localTagsFlow.emit(_localTags)
    }

    suspend fun insertCustomTagData(customTagEntity: CustomTagEntity) {

    }

    suspend fun insertLocalTagsOnSave(noteId: Int) {
        _localTags.forEach {
            customTagDao.insertTag(
                customTagEntity = CustomTagEntity(
                    tagName = it
                )
            )
        }
    }

    fun getTagsByNoteId(noteId: Int): Flow<List<CustomTagEntity>> =
        customTagDao.geTagsByNoteId(noteId)

    suspend fun setTagsByNoteId(customTagEntity: List<CustomTagEntity>) {
        // _tagsStateFlow.emit(customTagEntity)
    }

    fun getAllTags(): Flow<List<CustomTagEntity>> {
        return customTagDao.getAllTags()
    }

    suspend fun deleteTagById(tagId: Int) {
        customTagDao.deleteTagById(tagId)
    }

    suspend fun updateCustomTagData(customTagEntity: CustomTagEntity) {
        customTagDao.updateTag(customTagEntity.id, customTagEntity.tagName)
    }


}


