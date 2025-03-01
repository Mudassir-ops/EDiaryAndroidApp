package com.example.neweasydairy.fragments.tags

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.CustomTagEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val tagsRepository: TagsRepository
) : ViewModel() {

    val allTags = tagsRepository.getAllTags().asLiveData()

    fun insertCustomTagData(
        customTagName: String,
        ) {
        viewModelScope.launch {
            val customTagEntity = CustomTagEntity(tagName = customTagName)
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

}


