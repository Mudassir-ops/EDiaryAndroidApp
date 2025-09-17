package com.example.neweasydairy.fragments.editName

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentEditNameBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageRepository
import com.example.neweasydairy.fragments.languageFragment.LanguageViewModel
import com.example.neweasydairy.fragments.noteFragment.showPermissionDialog
import com.example.neweasydairy.utilis.saveImageToSpecificFolder
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class EditNameFragment : Fragment() {
    private var _binding: FragmentEditNameBinding? = null
    private val binding get() = _binding
    private lateinit var requestGalleryPermission: ActivityResultLauncher<String>
    private var galleryPermissionDeniedCount = 0
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private val editNameViewModel: EditNameViewModel by activityViewModels()
    private val viewModel: LanguageViewModel by activityViewModels()

    @Inject
    lateinit var languageRepository: LanguageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestGalleryPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    galleryPermissionDeniedCount = 0
                    pickImageLauncher.launch("image/*")
                } else {
                    galleryPermissionDeniedCount++
                    if (galleryPermissionDeniedCount >= 2) {
                        showPermissionDialog(
                            context = context ?: return@registerForActivityResult,
                            fragment = this
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Gallery permission is required to pick an image.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val folderName = "ProfileImages"
                val fileName = "profile_image.jpg"
                val savedPath =
                    requireContext().saveImageToSpecificFolder(uri, folderName, fileName)

                if (savedPath != null) {
                    editNameViewModel.saveProfileImage(savedPath)
                    //    editNameViewModel.imagePath.value = savedPath
                    Log.e(
                        "imagePath",
                        "onCreate: imagePath EditName ${editNameViewModel.imagePath.value}",
                    )
                    Glide.with(this)
                        .load(savedPath)
                        .signature(ObjectKey(System.currentTimeMillis()))
                        .skipMemoryCache(true)
                        .into(binding?.profileImage ?: return@registerForActivityResult)
                } else {
                    Glide.with(this)
                        .load(R.drawable.ic_profile_new)
                        .into(binding?.profileImage ?: return@registerForActivityResult)
                }
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNameBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            setupClickListeners()
            observeViewModel()
        }
    }

    private fun FragmentEditNameBinding.setupClickListeners() {


        binding?.icBack?.setOnClickListener {
            findNavController().navigateUp()
        }
        val userName = languageRepository.getUserName()
        if (userName != null) {
            binding?.edTextName?.setText(userName)
        }

        icEditProfile.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestGalleryPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                requestGalleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        btnDone.setOnClickListener {
            val newName = edTextName.text.toString()
            if (newName.isNotEmpty()) {
                viewModel.setUserName(newName)
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun FragmentEditNameBinding.observeViewModel() {
        viewModel.isUserName.observe(viewLifecycleOwner) { userName ->
            edTextName.setText(userName)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
