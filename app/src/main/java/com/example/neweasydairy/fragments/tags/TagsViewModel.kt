package com.example.neweasydairy.fragments.tags

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.CustomTagEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val tagsRepository: TagsRepository
) : ViewModel() {

    val tagsStateFlow: SharedFlow<List<String>> = tagsRepository.localTagsFlow

    fun insertLocalTag(tag: String) {
        viewModelScope.launch {
            tagsRepository.insertLocalTag(tag)
        }
    }

    fun addAllTagsForCreatedNote(allTags: List<String>) {
        viewModelScope.launch {
            tagsRepository.addAllTagsForCreatedNote(allTags)
        }
    }

    fun removeTag(tag: String) {
        viewModelScope.launch {
            tagsRepository.removeTag(tag)
        }
    }

    fun clearLocalTags() {
        viewModelScope.launch {
            tagsRepository.clearLocalTags()
        }
    }


    val allTags = tagsRepository.getAllTags().asLiveData()
    fun insertCustomTagData(customTagEntity: CustomTagEntity) {
        viewModelScope.launch {
            tagsRepository.insertCustomTagData(customTagEntity)
        }
    }

    fun deleteCustomTagById(tagId: Int) {
        viewModelScope.launch {
            tagsRepository.deleteTagById(tagId)
        }
    }

    fun updateCustomTagData(customTagEntity: CustomTagEntity) {
        viewModelScope.launch {
            tagsRepository.updateCustomTagData(customTagEntity)
        }
    }

    fun getTagsByNoteId(noteId: Int, lifecycle: Lifecycle) {
        viewModelScope.launch {
            tagsRepository.getTagsByNoteId(noteId).flowWithLifecycle(lifecycle = lifecycle)
                .collect {
                    tagsRepository.setTagsByNoteId(customTagEntity = it)
                }
        }
    }
}


