package com.example.neweasydairy.fragments.homeFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.NotePadDao
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.data.SettingsDao
import com.example.neweasydairy.data.SettingsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val settingsDao: SettingsDao,
    private val notePadDao: NotePadDao
) : ViewModel() {

    private val _allNotes = MutableStateFlow<NotesStates>(NotesStates.Init)
    val allNotes: SharedFlow<NotesStates> = _allNotes

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getSortedNotesFlow() {
        settingsDao.getSettingsFlow().distinctUntilChanged().flatMapLatest { settings ->
            val sorting = settings?.sortingOrder ?: false
            _allNotes.value = NotesStates.SortingOrder(sorting)
            Log.d("getAllNotesSatti--->", "getAllNotes: $sorting")
            if (sorting) {
                notePadDao.getNotesSortedByOldest()
            } else {
                notePadDao.getNotesSortedByLatest()
            }
        }.onEach { notes ->
            _allNotes.value = NotesStates.AllNotes(notes)
        }.launchIn(viewModelScope)
    }

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

    fun toggleSortingOrder() {
        viewModelScope.launch {
            val currentSettings = settingsDao.getSettings()
            val newSorting = !(currentSettings?.sortingOrder ?: false)
            settingsDao.insertSortingOrderData(
                SettingsEntity(id = 1, sortingOrder = newSorting)
            )
        }
    }


}

sealed interface NotesStates {
    data object Init : NotesStates
    data class AllNotes(val allNotes: List<NotepadEntity>) : NotesStates
    data class SortingOrder(val sortingOrder: Boolean) : NotesStates
}
