package com.example.neweasydairy.fragments.editName

import android.content.SharedPreferences
import jakarta.inject.Inject
import androidx.core.content.edit

class EditNameRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_PROFILE_IMAGE_PATH = "key_profile_image_path"
    }

    fun saveProfileImagePath(path: String) {
        sharedPreferences.edit() { putString(KEY_PROFILE_IMAGE_PATH, path) }
    }

    fun getProfileImagePath(): String? {
        return sharedPreferences.getString(KEY_PROFILE_IMAGE_PATH, null)
    }

}