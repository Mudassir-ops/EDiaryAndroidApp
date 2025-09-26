package com.example.neweasydairy.fragments.permissionFragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentPermissionBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageViewModel
import com.example.neweasydairy.fragments.noteFragment.showPermissionDialog
import com.example.neweasydairy.utilis.requiredPermissions


class PermissionFragment : Fragment() {
    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding
    val languageViewModel: LanguageViewModel by activityViewModels()

    private var cameraDeniedCount = 0
    private var galleryDeniedCount = 0

    private lateinit var requestCameraPermission: ActivityResultLauncher<String>
    private lateinit var requestGalleryPermission: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCameraPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    binding?.icSwitchGallery?.setImageResource(R.drawable.ic_switch_on)
                    cameraDeniedCount = 0
                } else {
                    binding?.icSwitchGallery?.setImageResource(R.drawable.ic_switch_off)
                    cameraDeniedCount++
                    if (cameraDeniedCount >= 2) {
                        showPermissionDialog(context ?: return@registerForActivityResult, this)
                    }
                }
                checkAllPermissions()
            }
        requestGalleryPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
                val allGranted = results.all { it.value }
                if (allGranted) {
                    binding?.icSwitchGallery?.setImageResource(R.drawable.ic_switch_on)
                    galleryDeniedCount = 0
                } else {
                    binding?.icSwitchGallery?.setImageResource(R.drawable.ic_switch_off)
                    galleryDeniedCount++
                    if (galleryDeniedCount >= 2) {
                        showPermissionDialog(context ?: return@registerForActivityResult, this)
                    }
                }
                checkAllPermissions()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnDone?.isEnabled = false
        binding?.btnDone?.alpha = 0.5f

        binding?.icSwitchCamera?.setOnClickListener {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }

        binding?.icSwitchGallery?.setOnClickListener {
            val permissions = mutableListOf<String>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            requestGalleryPermission.launch(permissions.toTypedArray())
        }

        binding?.btnDone?.setOnClickListener {
            languageViewModel.setDoneButtonPermissionScreen(true)
            if (findNavController().currentDestination?.id == R.id.permissionFragment) {
                findNavController().navigate(R.id.action_permissionFragment_to_nameFragment)
            }
        }
    }

    private fun checkAllPermissions() {
        val cameraGranted =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED

        val galleryGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            val read = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) ==
                    PackageManager.PERMISSION_GRANTED
            val write = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) ==
                    PackageManager.PERMISSION_GRANTED
            read && write
        }

        val allGranted = cameraGranted && galleryGranted
        binding?.btnDone?.isEnabled = allGranted
        binding?.btnDone?.alpha = if (allGranted) 1f else 0.5f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


