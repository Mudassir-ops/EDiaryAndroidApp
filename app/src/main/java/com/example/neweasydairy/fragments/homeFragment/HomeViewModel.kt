package com.example.neweasydairy.fragments.homeFragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.data.SettingsDao
import com.example.neweasydairy.data.SettingsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val settingsDao: SettingsDao
) : ViewModel() {

    val allNotes = homeRepository.getAllNotes().asLiveData()

    fun getNotesForDate(startOfDay: Long, endOfDay: Long): LiveData<List<NotepadEntity>> {
        val liveData = MutableLiveData<List<NotepadEntity>>()
        viewModelScope.launch {
            val notes = homeRepository.getNotesByDate(startOfDay, endOfDay)
            liveData.postValue(notes)
        }
        return liveData
    }

    fun deleteNote(note: NotepadEntity) {
        viewModelScope.launch {
            homeRepository.deleteNoteById(note.id)
        }
    }

    fun isRatingDialogShown(): Boolean {
        return homeRepository.isRatingDialogShown()
    }

    fun setRatingDialogShown() {
        homeRepository.setRatingDialogShown()
    }

    fun insertSortingOrder(settingsEntity: SettingsEntity) {
        viewModelScope.launch {
            settingsDao.insertSortingOrderData(notepadEntity = settingsEntity)
        }
    }

    suspend fun getSortingOrder(): SettingsEntity? {
        return settingsDao.getSettings()
    }

}
