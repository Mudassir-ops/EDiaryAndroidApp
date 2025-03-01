package com.example.neweasydairy.fragments.profileFragment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neweasydairy.fragments.languageFragment.LanguageDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val profileList = MutableLiveData<List<ProfileDataModel>?>()

    init {
        profileList.value = profileRepository.getProfileList()
    }

    fun getProfileList(): List<ProfileDataModel> {
        return profileRepository.getProfileList()
    }
}
