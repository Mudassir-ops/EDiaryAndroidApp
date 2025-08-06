package com.example.neweasydairy.fragments.tags

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.CustomTagEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val tagsRepository: TagsRepository
) : ViewModel() {

    val tagsStateFlow: SharedFlow<List<String>> = tagsRepository.localTagsFlow
    val allTagsFlow: SharedFlow<List<CustomTagEntity>> = tagsRepository.allTagsFlow

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

    fun getAllTags(lifecycle: Lifecycle) {
        viewModelScope.launch {
            tagsRepository.getAllTags(lifecycle)
        }
    }

    fun updateTag(noteId: Int, oldTag: CustomTagEntity?, newTag: CustomTagEntity?) {
        viewModelScope.launch {
            tagsRepository.updateTag(noteId = noteId, newTag = newTag, oldTag = oldTag)
        }
    }

}


