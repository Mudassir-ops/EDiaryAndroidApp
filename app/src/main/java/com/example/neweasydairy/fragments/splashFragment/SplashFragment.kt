package com.example.neweasydairy.fragments.splashFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentSplashBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding
    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var languageRepository: LanguageRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkNavigation()
    }

    private fun checkNavigation() {
        if (!isAdded) return
        val navController = findNavController()
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId != R.id.splashFragment) return

        val isPinPresent = getPinFromSharedPreferences()?.isEmpty() == false
        val isUserNamePresent = languageRepository.getUserName()?.isEmpty() == false

        val destination = if (isPinPresent) {
            if (isUserNamePresent) {
                R.id.action_splashFragment_to_welcomeFragment
            } else {
                R.id.action_splashFragment_to_introFragment
            }
        } else {
            R.id.action_splashFragment_to_introFragment
        }
        Log.d("SattiKsiHo", "checkNavigation: $isPinPresent")
        if (currentDestinationId == R.id.splashFragment) {
            if (view != null && isAdded) {
                navController.navigate(destination)
            }
        }
    }

    private fun getPinFromSharedPreferences(): String? {
        val sharedPreferences = context?.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("UserPin", null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
