package com.example.neweasydairy.fragments.editName

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditNameViewModel @Inject constructor(
    private val application: Application,
    private val editNameRepository: EditNameRepository
): ViewModel() {

    val imagePath = MutableLiveData<String?>()

    fun saveProfileImage(path: String) {
        editNameRepository.saveProfileImagePath(path)
        imagePath.value = path
    }

    fun loadProfileImage() {
        val savedPath = editNameRepository.getProfileImagePath()
        imagePath.value = savedPath
    }

}
