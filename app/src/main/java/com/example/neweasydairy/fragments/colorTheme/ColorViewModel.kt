package com.example.neweasydairy.fragments.colorTheme

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ColorViewModel @Inject constructor(
    private val colorRepository: ColorRepository
) : ViewModel() {

    val colorThemeList = MutableLiveData<List<ColorThemeDataModel>?>()

    init {
        colorThemeList.value = colorRepository.getColorThemeList()
    }
    fun getColorThemeList(): List<ColorThemeDataModel> {
        return colorRepository.getColorThemeList()
    }
}
