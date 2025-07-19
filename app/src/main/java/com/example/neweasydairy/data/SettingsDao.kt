package com.example.neweasydairy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSortingOrderData(notepadEntity: SettingsEntity)


    @Query("SELECT * FROM settings_table WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): SettingsEntity?

}
