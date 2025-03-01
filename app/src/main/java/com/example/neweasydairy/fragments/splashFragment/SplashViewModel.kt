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

    suspend fun getNextButtonIntroOne(): Boolean {
        return withContext(Dispatchers.IO) {
            languageRepository.getNextButtonIntroOne()
        }
    }

    suspend fun getNextButtonIntroTwo(): Boolean {
        return withContext(Dispatchers.IO) {
            languageRepository.getNextButtonIntroTwo()
        }
    }

    suspend fun getNextButtonIntroThree(): Boolean {
        return withContext(Dispatchers.IO) {
            languageRepository.getNextButtonIntroThree()
        }
    }

    suspend fun getDoneButtonPermission(): Boolean {
        return withContext(Dispatchers.IO) {
            languageRepository.getDoneButtonPermissionScreen()
        }
    }

    suspend fun  getNextButtonNameScreen(): Boolean {
        return withContext(Dispatchers.IO) {
            languageRepository.getNextButtonNameScreen()
        }
    }

    suspend fun getSelectButtonThemeScreen(): Boolean {
        return withContext(Dispatchers.IO) {
            languageRepository.getSelectButtonThemeScreen()
        }
    }

    suspend fun getPinButtonPinScreen(): Boolean {
        return withContext(Dispatchers.IO) {
            languageRepository.getPinButtonPinScreen()
        }
    }

    suspend fun getWelcomeButtonWelcomeScreen(): Boolean {
        return withContext(Dispatchers.IO) {
            languageRepository.getWelcomeButtonWelcomeScreen()
        }
    }


}
