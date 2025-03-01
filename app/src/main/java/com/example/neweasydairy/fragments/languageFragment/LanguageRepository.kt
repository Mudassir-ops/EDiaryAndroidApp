package com.example.neweasydairy.fragments.languageFragment

import android.content.SharedPreferences
import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.fragments.languageFragment.LanguageDataModel
import com.example.neweasydairy.utilis.BooleanObjects.introScreenOne
import com.example.neweasydairy.utilis.BooleanObjects.introScreenThree
import com.example.neweasydairy.utilis.BooleanObjects.introScreenTwo
import com.example.neweasydairy.utilis.BooleanObjects.languageDoneButton
import com.example.neweasydairy.utilis.BooleanObjects.nameScreen
import com.example.neweasydairy.utilis.BooleanObjects.permissionScreen
import com.example.neweasydairy.utilis.BooleanObjects.pinScreen
import com.example.neweasydairy.utilis.BooleanObjects.themeScreen
import com.example.neweasydairy.utilis.BooleanObjects.userName
import com.example.neweasydairy.utilis.BooleanObjects.welcomeScreen
import jakarta.inject.Inject


class LanguageRepository @Inject constructor( private val sharedPreferences: SharedPreferences) {

    fun getLanguageList() = listOf(
        LanguageDataModel(countryFlag = R.drawable.china_flag, languageName ="English (US)", languageCode ="us",  isSelected =false),
        LanguageDataModel(countryFlag = R.drawable.china_flag, languageName ="English (UK)", languageCode ="us",  isSelected =false),
        LanguageDataModel(countryFlag = R.drawable.china_flag, languageName ="Turkish", languageCode ="us",  isSelected =false),
        LanguageDataModel(countryFlag = R.drawable.china_flag, languageName ="Arabic ", languageCode ="us",  isSelected =false),
        LanguageDataModel(countryFlag = R.drawable.china_flag, languageName ="Portuguese", languageCode ="us",  isSelected =false),
        LanguageDataModel(countryFlag = R.drawable.china_flag, languageName ="Spanish", languageCode ="us",  isSelected =false),
    )

    fun setDoneValue(isDoneClick: Boolean) {
        sharedPreferences.edit().putBoolean(languageDoneButton, isDoneClick).apply()
    }

    fun getDoneValue(): Boolean {
        return sharedPreferences.getBoolean(languageDoneButton, false)
    }

    fun setNextButtonIntroOne(isDoneClick: Boolean) {
        sharedPreferences.edit().putBoolean(introScreenOne, isDoneClick).apply()
    }

    fun getNextButtonIntroOne(): Boolean {
        return sharedPreferences.getBoolean(introScreenOne, false)
    }


    fun setNextButtonIntroTwo(isDoneClick: Boolean) {
        sharedPreferences.edit().putBoolean(introScreenTwo, isDoneClick).apply()
    }

    fun getNextButtonIntroTwo(): Boolean {
        return sharedPreferences.getBoolean(introScreenTwo, false)
    }

    fun setNextButtonIntroThree(isDoneClick: Boolean) {
        sharedPreferences.edit().putBoolean(introScreenThree, isDoneClick).apply()
    }

    fun getNextButtonIntroThree(): Boolean {
        return sharedPreferences.getBoolean(introScreenThree, false)
    }

    fun setDoneButtonPermissionScreen(isDoneClick: Boolean) {
        sharedPreferences.edit().putBoolean(permissionScreen, isDoneClick).apply()
    }

    fun getDoneButtonPermissionScreen(): Boolean {
        return sharedPreferences.getBoolean(permissionScreen, false)
    }

    fun setNextButtonNameScreen(isDoneClick: Boolean) {
        sharedPreferences.edit().putBoolean(nameScreen, isDoneClick).apply()
    }

    fun getNextButtonNameScreen(): Boolean {
        return sharedPreferences.getBoolean(nameScreen, false)
    }

    fun setSelectButtonThemeScree(isDoneClick: Boolean) {
        sharedPreferences.edit().putBoolean(themeScreen, isDoneClick).apply()
    }

    fun getSelectButtonThemeScreen(): Boolean {
        return sharedPreferences.getBoolean(themeScreen, false)
    }

    fun setPinButtonPinScreen(isDoneClick: Boolean) {
        sharedPreferences.edit().putBoolean(pinScreen, isDoneClick).apply()
    }

    fun getPinButtonPinScreen(): Boolean {
        return sharedPreferences.getBoolean(pinScreen, false)
    }

    fun setWelcomeButtonWelcomeScree(isDoneClick: Boolean) {
        sharedPreferences.edit().putBoolean(welcomeScreen, isDoneClick).apply()
    }

    fun getWelcomeButtonWelcomeScreen(): Boolean {
        return sharedPreferences.getBoolean(welcomeScreen, false)
    }

    fun setUserName(username: String) {
        sharedPreferences.edit().putString(userName, username).apply()
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(userName, null)
    }
}