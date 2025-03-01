package com.example.neweasydairy.fragments.permissionFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentPermissionBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageViewModel
import com.example.neweasydairy.fragments.noteFragment.showPermissionDialog


class PermissionFragment : Fragment() {
    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding
    lateinit var requestCameraPermission: ActivityResultLauncher<String>
    lateinit var requestGalleryPermission: ActivityResultLauncher<String>

    private var cameraPermissionDeniedCount = 0
    private var galleryPermissionDeniedCount = 0
    val languageViewModel:LanguageViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCameraPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    binding?.icSwitchCamera?.setImageResource(R.drawable.ic_switch_on)
                    cameraPermissionDeniedCount = 0
                } else {
                    binding?.icSwitchCamera?.setImageResource(R.drawable.ic_switch_off)
                    cameraPermissionDeniedCount++
                    if (cameraPermissionDeniedCount >= 2) {
                        showPermissionDialog(
                            context = context ?: return@registerForActivityResult,
                            fragment = this
                        )
                    }
                }
            }

        requestGalleryPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    binding?.icSwitchGallery?.setImageResource(R.drawable.ic_switch_on)
                    galleryPermissionDeniedCount = 0
                } else {
                    binding?.icSwitchGallery?.setImageResource(R.drawable.ic_switch_off)
                    galleryPermissionDeniedCount++
                    if (galleryPermissionDeniedCount >= 2) {
                        showPermissionDialog(
                            context = context ?: return@registerForActivityResult,
                            fragment = this
                        )
                    }
                }
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
        binding?.apply {
            permissionClickListener(this@PermissionFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}