package com.example.neweasydairy.fragments.homeFragment

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.data.SettingsEntity
import com.example.neweasydairy.utilis.Objects.CHECK_NAVIGATION
import com.example.neweasydairy.utilis.Objects.FROM_ICON_ADD_NOTE
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun clickListener(homeFragment: HomeFragment) {
    homeFragment.binding?.apply {
        icAddNotes.setOnClickListener {
            homeFragment.createNoteViewModel.currentNoteId = null
            val bundle = Bundle()
            bundle.putString(CHECK_NAVIGATION, FROM_ICON_ADD_NOTE)
            homeFragment.findNavController().navigate(R.id.createNotesFragment, bundle)
        }
        icSorting.setOnClickListener {
            sortNotes(homeFragment)
            rotateIcon(homeFragment)
        }
    }
}

private fun sortNotes(homeFragment: HomeFragment) {
    homeFragment.homeViewModel.allNotes.value?.let { notes ->
        homeFragment.viewLifecycleOwner.lifecycleScope.launch {
            val sortingOrder = homeFragment.homeViewModel.getSortingOrder()
            withContext(Main) {
                val sortedNotes = if (sortingOrder?.sortingOrder == true) {
                    notes.sortedBy { it.timestamp }
                } else {
                    notes.sortedByDescending { it.timestamp }
                }
                homeFragment.isAscending = !homeFragment.isAscending
                homeFragment.homeViewModel.insertSortingOrder(
                    SettingsEntity(
                        id = 1,
                        sortingOrder = homeFragment.isAscending
                    )
                )
                homeFragment.homeAdapter.updateList(sortedNotes)
            }

        }
    }
}

private fun rotateIcon(homeFragment: HomeFragment) {
    homeFragment.binding?.icSorting?.animate()?.rotation(homeFragment.currentRotation + 180f)
        ?.setDuration(300)?.start()
    homeFragment.currentRotation += 180f
}