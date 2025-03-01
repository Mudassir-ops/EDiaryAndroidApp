package com.example.neweasydairy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM reminder_table")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Query("DELETE FROM reminder_table WHERE id = :reminderId")
    suspend fun deleteReminderById(reminderId: Int)
}
