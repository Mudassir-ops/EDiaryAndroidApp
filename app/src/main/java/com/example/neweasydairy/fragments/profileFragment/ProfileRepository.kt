package com.example.neweasydairy.fragments.profileFragment

import com.example.easydiaryandjournalwithlock.R
import jakarta.inject.Inject

class ProfileRepository @Inject constructor() {
    fun getProfileList() = listOf(
        ProfileDataModel(icon = R.drawable.ic_edit_tags, title = "Edit Tags"),
        ProfileDataModel(icon = R.drawable.ic_notification, title = "Reminders"),
        ProfileDataModel(icon = R.drawable.ic_dairy_lock, title = "Dairy Lock"),
        ProfileDataModel(icon = R.drawable.ic_share_app, title = "Share App"),
        ProfileDataModel(icon = R.drawable.ic_privacy_policy, title = "Feedback"),
        ProfileDataModel(icon = R.drawable.ic_privacy_policy, title = "Privacy Policy"),
    )
}