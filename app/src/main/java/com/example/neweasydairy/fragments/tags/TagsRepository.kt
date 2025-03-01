package com.example.neweasydairy.fragments.tags

import android.content.SharedPreferences
import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.data.CustomTagDao
import com.example.neweasydairy.data.CustomTagEntity
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.fragments.profileFragment.ProfileDataModel
import com.google.gson.Gson
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TagsRepository @Inject constructor(private val customTagDao: CustomTagDao) {

    suspend fun insertCustomTagData(customTagEntity: CustomTagEntity) {
        customTagDao.insertTag(customTagEntity)
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


