package com.example.neweasydairy.fragments.colorTheme

import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.fragments.languageFragment.LanguageDataModel
import jakarta.inject.Inject

class ColorRepository @Inject constructor() {

    fun getColorThemeList()= listOf(
        ColorThemeDataModel(colorThemeImage = R.drawable.color_theme_one),
        ColorThemeDataModel(colorThemeImage = R.drawable.color_theme_one),
        ColorThemeDataModel(colorThemeImage = R.drawable.color_theme_one),
    )
}