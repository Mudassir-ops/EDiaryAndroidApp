package com.example.neweasydairy.fragments.splashFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.neweasydairy.fragments.languageFragment.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : ViewModel() {

    suspend fun getIsDoneValue(): Boolean {
        return withContext(Dispatchers.IO) {
            languageRepository.getDoneValue()
        }
    }

    fun getNextButtonIntroOne(): Boolean {
        return languageRepository.getNextButtonIntroOne()
    }

    fun getNextButtonIntroTwo(): Boolean {
        return languageRepository.getNextButtonIntroTwo()
    }

    fun getNextButtonIntroThree(): Boolean {
        return languageRepository.getNextButtonIntroThree()
    }

    fun getDoneButtonPermission(): Boolean {
        return languageRepository.getDoneButtonPermissionScreen()
    }

    fun getNextButtonNameScreen(): Boolean {
        return languageRepository.getNextButtonNameScreen()
    }

    fun getSelectButtonThemeScreen(): Boolean {
        return languageRepository.getSelectButtonThemeScreen()
    }

    fun getPinButtonPinScreen(): Boolean {
        return languageRepository.getPinButtonPinScreen()
    }

    fun getWelcomeButtonWelcomeScreen(): Boolean {
        return languageRepository.getWelcomeButtonWelcomeScreen()
    }


}
