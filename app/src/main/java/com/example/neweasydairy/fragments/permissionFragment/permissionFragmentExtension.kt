package com.example.neweasydairy.fragments.permissionFragment

import android.Manifest
import android.os.Build
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentPermissionBinding

fun FragmentPermissionBinding?.permissionClickListener(fragment: PermissionFragment) {

    this?.apply {
        icSwitchCamera.setOnClickListener {
            fragment.requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
        icSwitchGallery.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
               fragment.requestGalleryPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                fragment.requestGalleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        btnDone.setOnClickListener {
            fragment.languageViewModel.setDoneButtonPermissionScreen(true)
            if (fragment.findNavController().currentDestination?.id==R.id.permissionFragment){
                fragment.findNavController().navigate(R.id.action_permissionFragment_to_nameFragment)
            }
        }
    }

}