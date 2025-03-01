package com.example.neweasydairy.fragments.mainFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.neweasydairy.fragments.languageFragment.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : ViewModel() {


}
