package com.example.neweasydairy.fragments.languageFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : ViewModel() {
    val languageList = MutableLiveData<List<LanguageDataModel>?>()


    private val _isUserName = MutableLiveData<String>()
    val isUserName: LiveData<String> get() = _isUserName

    init {
        languageList.value = languageRepository.getLanguageList()
        _isUserName.value = languageRepository.getUserName()
    }

    fun setUserName(userName: String) {
        languageRepository.setUserName(userName)
        _isUserName.value = userName

    }

    fun getLanguageList(): List<LanguageDataModel> {
        return languageRepository.getLanguageList()
    }

    fun setDoneValue(isDone: Boolean) {
        languageRepository.setDoneValue(isDone)
    }


    fun setNextButtonIntroOne(isNextButtonIntroOneClick: Boolean) {
        languageRepository.setNextButtonIntroOne(isNextButtonIntroOneClick)
    }

    fun setNextButtonIntroTwo(isNextButtonIntroTwoClick: Boolean) {
        languageRepository.setNextButtonIntroTwo(isNextButtonIntroTwoClick)
    }

    fun setNextButtonIntroThree(isNextButtonIntroThreeClick: Boolean) {
        languageRepository.setNextButtonIntroThree(isNextButtonIntroThreeClick)
    }

    fun setDoneButtonPermissionScreen(isDoneButtonPermissionScreen: Boolean) {
        languageRepository.setDoneButtonPermissionScreen(isDoneButtonPermissionScreen)
    }

    fun setNextButtonNameScreen(isNextButtonNameScreen: Boolean) {
        languageRepository.setNextButtonNameScreen(isNextButtonNameScreen)
    }

    fun setSelectButtonThemeScreen(isSelectButtonThemeScreen: Boolean) {
        languageRepository.setSelectButtonThemeScree(isSelectButtonThemeScreen)
    }


    fun setWelcomeButtonWelcomeScreen(isWelcomeButtonWelcomeScreen: Boolean) {
        languageRepository.setWelcomeButtonWelcomeScree(isWelcomeButtonWelcomeScreen)
    }


}
