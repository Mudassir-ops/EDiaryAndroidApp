package com.example.neweasydairy.fragments.mainFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentMainBinding
import com.example.neweasydairy.dialogs.DeleteAccountDialog
import com.example.neweasydairy.dialogs.ExitDialog
import com.example.neweasydairy.dialogs.LogoutDialog
import com.example.neweasydairy.fragments.calendarFragment.CalendarFragment
import com.example.neweasydairy.fragments.editName.EditNameViewModel
import com.example.neweasydairy.fragments.homeFragment.HomeFragment
import com.example.neweasydairy.fragments.languageFragment.LanguageRepository
import com.example.neweasydairy.fragments.libraryFragment.LibraryFragment
import com.example.neweasydairy.fragments.profileFragment.ProfileAdapter
import com.example.neweasydairy.fragments.profileFragment.ProfileViewModel
import com.example.neweasydairy.utilis.feedBackWithEmail
import com.example.neweasydairy.utilis.privacyPolicyUrl
import com.example.neweasydairy.utilis.shareApp
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding
    var exitDialog: ExitDialog? = null
    private var logoutDialog: LogoutDialog? = null
    private var deleteAccountDialog: DeleteAccountDialog? = null
    private var adapter: ProfileAdapter? = null
    private val viewModel: ProfileViewModel by viewModels()
    private val editNameViewModel: EditNameViewModel by activityViewModels()

    @Inject
    lateinit var languageRepository: LanguageRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitDialog = ExitDialog(activity ?: return)
        logoutDialog = LogoutDialog(activity ?: return)
        deleteAccountDialog = DeleteAccountDialog(activity ?: return)
        adapter = ProfileAdapter(
            context = context ?: return,
            profileList = viewModel.getProfileList(),
            onItemSelected = { itemSelected ->
                when (itemSelected) {
                    0 -> {
                        binding?.mainDrawerLayout?.closeDrawer(GravityCompat.START)
                        if (findNavController().currentDestination?.id == R.id.mainFragment) {
                            findNavController().navigate(R.id.action_mainFragment_to_tagsFragment)
                        }
                    }

                    1 -> {
                        binding?.mainDrawerLayout?.closeDrawer(GravityCompat.START)
                        if (findNavController().currentDestination?.id == R.id.mainFragment) {
                            findNavController().navigate(R.id.action_mainFragment_to_reminderFragment)
                        }
                    }

                    2 -> {
                        binding?.mainDrawerLayout?.closeDrawer(GravityCompat.START)
                        if (findNavController().currentDestination?.id == R.id.mainFragment) {
                            findNavController().navigate(R.id.action_mainFragment_to_changePinFragment)
                        }
                    }

                    3 -> {
                        this.activity?.shareApp()
                        binding?.mainDrawerLayout?.closeDrawer(GravityCompat.START)
                    }

                    4 -> {
                        context?.feedBackWithEmail(
                            title = "Feedback",
                            message = "Any Feedback",
                            emailId = "Cisco7865@gmail.com"
                        )
                        binding?.mainDrawerLayout?.closeDrawer(GravityCompat.START)

                    }

                    5 -> {
                        activity?.privacyPolicyUrl()
                        binding?.mainDrawerLayout?.closeDrawer(GravityCompat.START)

                    }

                    else -> {
                        Log.e("profile", "onCreate: else")
                    }
                }


            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding?.apply {
                    if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mainDrawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        val currentFragment =
                            childFragmentManager.findFragmentById(R.id.frameLayout)
                        if (currentFragment !is HomeFragment) {
                            replaceFragment(HomeFragment())
                        } else {
                            exitDialog?.show()
                        }
                    }
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editNameViewModel.loadProfileImage()

        editNameViewModel.imagePath.observe(viewLifecycleOwner) { imagePath ->
            Log.e("imagePath", "onCreate: imagePath MainFragment $imagePath")

            imagePath?.let {
                Glide.with(this)
                    .load(it)
                    .signature(ObjectKey(System.currentTimeMillis()))
                    .skipMemoryCache(true)
                    .into(binding?.drawerLayout?.icProfile ?: return@observe)
            }
        }
        val userName = languageRepository.getUserName()
        if (userName != null) {
            binding?.drawerLayout?.txtName?.text = userName.split(" ").joinToString(" ") {
                it.replaceFirstChar { char -> char.uppercaseChar() }
            }
        }
        binding?.apply {
            setUpHeaderLayout()
            drawerLayout.txtEdit.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.mainFragment) {
                    findNavController().navigate(R.id.action_mainFragment_to_editNameFragment)
                }

            }

        }
        replaceFragment(HomeFragment())
        clickListener()
        observerViewModel()
    }

    private fun clickListener() {
        binding?.apply {
            bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> {
                        replaceFragment(HomeFragment())
                        binding?.txtHome?.text = "Home"
                    }

                    R.id.library -> {
                        replaceFragment(LibraryFragment())
                        binding?.txtHome?.text = "Library"
                    }

                    R.id.calendar -> {
                        replaceFragment(CalendarFragment())
                        binding?.txtHome?.text = "Calendar"
                    }

                    else -> false
                }
                true
            }

            icNotification.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.mainFragment) {
                    findNavController().navigate(R.id.action_mainFragment_to_reminderFragment)
                }
            }
        }
    }

    private fun observerViewModel() {
        viewModel.profileList.observe(viewLifecycleOwner) { profileList ->
            if (!profileList.isNullOrEmpty()) {
                adapter?.updateProfileList(profileList)
            }
            if (binding?.drawerLayout?.drawerRecyclerView?.adapter == null)
                binding?.drawerLayout?.drawerRecyclerView?.adapter = adapter
        }
    }


    private fun replaceFragment(fragment: Fragment): Boolean {
        childFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
        return true
    }


    override fun onResume() {
        super.onResume()
        binding?.mainDrawerLayout?.let {
            if (it.isDrawerOpen(GravityCompat.START)) {
                it.closeDrawer(GravityCompat.START)
            }
        }
    }

}