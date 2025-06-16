package com.example.neweasydairy.fragments.reminderFragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {
    val allReminders = reminderRepository.getAllReminder().asLiveData()

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    private val _description = MutableLiveData<String>()
    private val _isToggleClick = MutableLiveData<Boolean>()
    val isToggleClick: LiveData<Boolean> get() = _isToggleClick


    val description: LiveData<String> get() = _description

    init {
        _title.value = reminderRepository.getTitle()
        _description.value = reminderRepository.getDescription()
        _isToggleClick.value = reminderRepository.getReminderToggle()
    }
    fun setReminderToggle(isDone: Boolean) {
        reminderRepository.setReminderToggle(isDone)
        _isToggleClick.value = isDone
    }
    suspend fun getReminderToggle(): Boolean {
        return withContext(Dispatchers.IO) {
            reminderRepository.getReminderToggle()
        }
    }

    fun setReminderData(title: String, description: String) {
        reminderRepository.setReminderData(title, description)
        _title.value = title
        _description.value = description
    }

    fun deleteReminderById(tagId: Int) {
        viewModelScope.launch {
            reminderRepository.deleteReminderById(tagId)
        }
    }


}
