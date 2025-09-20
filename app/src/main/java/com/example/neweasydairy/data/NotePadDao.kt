package com.example.neweasydairy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.neweasydairy.fragments.noteFragment.imageFunctionality.ImageDataModelGallery
import com.example.neweasydairy.typeConverter.ImageConverter
import kotlinx.coroutines.flow.Flow

@Dao
interface NotePadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteData(notepadEntity: NotepadEntity): Long

    @Query("delete from note_data_table")
    suspend fun deleteAllNotepad()

    @Query("DELETE FROM note_data_table WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Query("SELECT * FROM note_data_table WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NotepadEntity?

    @Query("SELECT * FROM note_data_table")
    fun getAllNotes(): Flow<List<NotepadEntity>>

    @Query("SELECT * FROM note_data_table WHERE noteTitle = :title LIMIT 1")
    suspend fun getNoteByTitle(title: String): NotepadEntity?

    @Update
    suspend fun update(note: NotepadEntity)

    @Query("SELECT * FROM note_data_table WHERE timestamp BETWEEN :startOfDay AND :endOfDay")
    suspend fun getNotesForDate(startOfDay: Long, endOfDay: Long): List<NotepadEntity>

    @Query("UPDATE note_data_table SET tagsList = :tagsList WHERE id = :noteId")
    suspend fun updateTag(noteId: Int, tagsList: String)

    @Query("select * from note_data_table")
    fun getAllNotesTags(): Flow<List<NotepadEntity>>


    @Query("SELECT * FROM note_data_table ORDER BY timestamp DESC")
    fun getNotesSortedByLatest(): Flow<List<NotepadEntity>>

    @Query("SELECT * FROM note_data_table ORDER BY timestamp ASC")
    fun getNotesSortedByOldest(): Flow<List<NotepadEntity>>

}
