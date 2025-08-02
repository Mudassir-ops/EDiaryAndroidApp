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
    private var exitDialog: ExitDialog? = null
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
                binding?.mainDrawerLayout?.closeDrawer(GravityCompat.START)
                val navController = findNavController()
                when (itemSelected) {
                    0 -> if (navController.currentDestination?.id == R.id.mainFragment)
                        navController.navigate(R.id.action_mainFragment_to_tagsFragment)

                    1 -> if (navController.currentDestination?.id == R.id.mainFragment)
                        navController.navigate(R.id.action_mainFragment_to_reminderFragment)

                    2 -> if (navController.currentDestination?.id == R.id.mainFragment)
                        navController.navigate(R.id.action_mainFragment_to_changePinFragment)

                    3 -> activity?.shareApp()

                    4 -> context?.feedBackWithEmail(
                        title = "Feedback",
                        message = "Any Feedback",
                        emailId = "Cisco7865@gmail.com"
                    )

                    5 -> activity?.privacyPolicyUrl()

                    else -> Log.e("profile", "Unhandled drawer item: $itemSelected")
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
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding?.let {
                    if (it.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        it.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        val currentFragment =
                            childFragmentManager.findFragmentById(R.id.frameLayout)
                        if (currentFragment !is HomeFragment) {
                            replaceFragment(HomeFragment())
                            it.bottomNavigation.selectedItemId = R.id.home
                        } else {
                            exitDialog?.show()
                        }
                    }
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        setupObservers()
        setupBottomNavigation()
        replaceFragment(HomeFragment())
        binding?.icMenu?.setOnClickListener {
            binding?.mainDrawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    private fun setupHeader() {
        binding?.apply {
            editNameViewModel.loadProfileImage()
            editNameViewModel.imagePath.observe(viewLifecycleOwner) { imagePath ->
                imagePath?.let {
                    Glide.with(this@MainFragment)
                        .load(it)
                        .signature(ObjectKey(System.currentTimeMillis()))
                        .skipMemoryCache(true)
                        .into(drawerLayout.icProfile)
                }
            }

            languageRepository.getUserName()?.let { name ->
                drawerLayout.txtName.text = name.split(" ")
                    .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
            }

            drawerLayout.txtEdit.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.mainFragment) {
                    findNavController().navigate(R.id.action_mainFragment_to_editNameFragment)
                }
            }

            drawerLayout.drawerRecyclerView.adapter = adapter
        }
    }

    private fun setupObservers() {
        viewModel.profileList.observe(viewLifecycleOwner) { profileList ->
            if (!profileList.isNullOrEmpty()) {
                adapter?.updateProfileList(profileList)
            }
        }
    }

    private fun setupBottomNavigation() {
        binding?.bottomNavigation?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    binding?.txtHome?.text = resources.getString(R.string.home)
                    true
                }

                R.id.library -> {
                    replaceFragment(LibraryFragment())
                    binding?.txtHome?.text = resources.getString(R.string.library)
                    true
                }

                R.id.calendar -> {
                    replaceFragment(CalendarFragment())
                    binding?.txtHome?.text = resources.getString(R.string.calendar)
                    true
                }

                else -> false
            }
        }

        binding?.icNotification?.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.mainFragment) {
                findNavController().navigate(R.id.action_mainFragment_to_reminderFragment)
            }
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
        binding?.mainDrawerLayout?.closeDrawer(GravityCompat.START)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

