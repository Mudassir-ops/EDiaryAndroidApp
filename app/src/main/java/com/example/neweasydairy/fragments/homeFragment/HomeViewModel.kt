package com.example.neweasydairy.fragments.homeFragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.NotepadEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
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
}
