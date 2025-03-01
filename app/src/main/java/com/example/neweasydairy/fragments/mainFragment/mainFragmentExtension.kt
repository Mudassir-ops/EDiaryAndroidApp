package com.example.neweasydairy.fragments.mainFragment

import androidx.core.view.GravityCompat
import com.example.easydiaryandjournalwithlock.databinding.FragmentMainBinding

fun FragmentMainBinding?.setUpHeaderLayout() {
    this?.icMenu?.setOnClickListener {
        mainDrawerLayout.openDrawer(GravityCompat.START)
    }
}