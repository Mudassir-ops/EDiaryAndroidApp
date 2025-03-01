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


    private val _isDoneClick = MutableLiveData<Boolean>()
    private val _isNextButtonIntroOneClick = MutableLiveData<Boolean>()
    private val _isNextButtonIntroTwoClick = MutableLiveData<Boolean>()
    private val _isNextButtonIntroThreeClick = MutableLiveData<Boolean>()
    private val _isDoneButtonPermissionScreen = MutableLiveData<Boolean>()
    private val _isNextButtonNameScreen = MutableLiveData<Boolean>()
    private val _isSelectButtonThemeScreen = MutableLiveData<Boolean>()
    private val _isPinButtonPinScreen = MutableLiveData<Boolean>()
    private val _isWelcomeButtonWelcomeScreen = MutableLiveData<Boolean>()
    private val _isUserName = MutableLiveData<String>()
    val isUserName: LiveData<String> get() = _isUserName

    init {
        languageList.value = languageRepository.getLanguageList()
        _isDoneClick.value = languageRepository.getDoneValue()
        _isUserName.value = languageRepository.getUserName()
    }

    fun setUserName(userName:String){
        languageRepository.setUserName(userName)
        _isUserName.value = userName

    }
    fun getLanguageList(): List<LanguageDataModel> {
        return languageRepository.getLanguageList()
    }

    fun setDoneValue(isDone: Boolean) {
        languageRepository.setDoneValue(isDone)
        _isDoneClick.value = isDone
    }

    fun setNextButtonIntroOne(isNextButtonIntroOneClick:Boolean){
        languageRepository.setNextButtonIntroOne(isNextButtonIntroOneClick)
        _isNextButtonIntroOneClick.value = isNextButtonIntroOneClick

    }

    fun setNextButtonIntroTwo(isNextButtonIntroTwoClick:Boolean){
        languageRepository.setNextButtonIntroTwo(isNextButtonIntroTwoClick)
        _isNextButtonIntroTwoClick.value = isNextButtonIntroTwoClick
    }

    fun setNextButtonIntroThree(isNextButtonIntroThreeClick:Boolean){
        languageRepository.setNextButtonIntroThree(isNextButtonIntroThreeClick)
        _isNextButtonIntroThreeClick.value = isNextButtonIntroThreeClick
    }

    fun setDoneButtonPermissionScreen(isDoneButtonPermissionScreen:Boolean){
        languageRepository.setDoneButtonPermissionScreen(isDoneButtonPermissionScreen)
        _isDoneButtonPermissionScreen.value = isDoneButtonPermissionScreen
    }

    fun setNextButtonNameScreen(isNextButtonNameScreen:Boolean){
        languageRepository.setNextButtonNameScreen(isNextButtonNameScreen)
        _isNextButtonNameScreen.value = isNextButtonNameScreen
    }
    fun setSelectButtonThemeScreen(isSelectButtonThemeScreen:Boolean){
        languageRepository.setSelectButtonThemeScree(isSelectButtonThemeScreen)
        _isSelectButtonThemeScreen.value = isSelectButtonThemeScreen
    }

    fun setPinButtonPinScreen(isPinButtonPinScreen:Boolean){
        languageRepository.setPinButtonPinScreen(isPinButtonPinScreen)
        _isPinButtonPinScreen.value = isPinButtonPinScreen
    }

    fun setWelcomeButtonWelcomeScreen(isWelcomeButtonWelcomeScreen:Boolean){
        languageRepository.setWelcomeButtonWelcomeScree(isWelcomeButtonWelcomeScreen)
        _isWelcomeButtonWelcomeScreen.value = isWelcomeButtonWelcomeScreen
    }




}
