package com.example.neweasydairy.fragments.reminderFragment

import android.content.SharedPreferences
import com.example.neweasydairy.data.CustomTagEntity
import com.example.neweasydairy.data.ReminderDao
import com.example.neweasydairy.data.ReminderEntity
import com.example.neweasydairy.utilis.BooleanObjects.REMINDER_DESCRIPTION
import com.example.neweasydairy.utilis.BooleanObjects.REMINDER_TITLE
import com.example.neweasydairy.utilis.BooleanObjects.reminderDescription
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ReminderRepository @Inject
constructor(private val sharedPreferences: SharedPreferences,private val reminderDao: ReminderDao)
{
    fun setReminderData(title: String, description: String) {
        sharedPreferences.edit()
            .putString(REMINDER_TITLE, title)
            .putString(REMINDER_DESCRIPTION, description)
            .apply()
    }

    fun getTitle(): String? {
        return sharedPreferences.getString(REMINDER_TITLE, null)
    }

    fun getDescription(): String? {
        return sharedPreferences.getString(REMINDER_DESCRIPTION, null)
    }

    fun getAllReminder(): Flow<List<ReminderEntity>> {
        return reminderDao.getAllReminders()
    }
    suspend fun deleteReminderById(tagId: Int) {
        reminderDao.deleteReminderById(tagId)
    }
}