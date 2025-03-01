package com.example.neweasydairy.fragments.editName

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditNameViewModel @Inject constructor(
    private val application: Application
): ViewModel() {

    val imagePath = MutableLiveData<String>()
}
