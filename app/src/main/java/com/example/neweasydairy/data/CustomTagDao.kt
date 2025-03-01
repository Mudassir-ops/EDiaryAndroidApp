package com.example.neweasydairy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(customTagEntity: CustomTagEntity)

    @Query("DELETE FROM custom_tag_table WHERE id = :tagId")
    suspend fun deleteTagById(tagId: Int)

    @Query("SELECT * FROM custom_tag_table")
    fun getAllTags(): Flow<List<CustomTagEntity>>

    @Query("UPDATE custom_tag_table SET tagName = :tagName WHERE id = :tagId")
    suspend fun updateTag(tagId: Int, tagName: String)
}
