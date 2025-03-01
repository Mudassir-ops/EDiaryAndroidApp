package com.example.neweasydairy.fragments.homeFragment

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentHomeBinding
import com.example.neweasydairy.utilis.Objects.CHECK_NAVIGATION
import com.example.neweasydairy.utilis.Objects.FROM_HOME_FRAGMENT
import com.example.neweasydairy.utilis.Objects.FROM_ICON_ADD_NOTE

fun FragmentHomeBinding?.clickListener(homeFragment: HomeFragment){
   homeFragment.binding?.apply {
        icAddNotes.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CHECK_NAVIGATION, FROM_ICON_ADD_NOTE)
            homeFragment.findNavController().navigate(R.id.createNotesFragment,bundle)

        }

        icSorting.setOnClickListener {
            sortNotes(homeFragment)
            rotateIcon(homeFragment)
        }
    }
}

private fun sortNotes(homeFragment: HomeFragment) {
   homeFragment.homeViewModel.allNotes.value?.let { notes ->
        val sortedNotes = if (homeFragment.isAscending) {
            notes.sortedBy { it.timestamp }
        } else {
            notes.sortedByDescending { it.timestamp }
        }
        homeFragment.isAscending = !homeFragment.isAscending
        homeFragment.homeAdapter.updateList(sortedNotes)
    }
}
private fun rotateIcon(homeFragment: HomeFragment) {
   homeFragment.binding?.icSorting?.animate()?.rotation(homeFragment.currentRotation + 180f)?.setDuration(300)?.start()
    homeFragment.currentRotation += 180f
}