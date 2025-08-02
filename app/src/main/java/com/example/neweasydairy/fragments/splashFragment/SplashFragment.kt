package com.example.neweasydairy.fragments.splashFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding
    private val viewModel: SplashViewModel by viewModels()

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
        val navController = findNavController()
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId != R.id.splashFragment) return
        val destination = when {
            !viewModel.getNextButtonIntroOne() -> R.id.action_splashFragment_to_introFragment
            !viewModel.getNextButtonIntroTwo() -> R.id.action_splashFragment_to_introFragmentTwo
            !viewModel.getNextButtonIntroThree() -> R.id.action_splashFragment_to_introFragmentThree
            !viewModel.getDoneButtonPermission() -> R.id.action_splashFragment_to_permissionFragment
            !viewModel.getNextButtonNameScreen() -> R.id.action_splashFragment_to_nameFragment
            !viewModel.getPinButtonPinScreen() -> R.id.action_splashFragment_to_pinFragment
            viewModel.getPinButtonPinScreen() -> R.id.action_splashFragment_to_welcomeFragment
            else -> R.id.action_splashFragment_to_mainFragment
        }
        if (currentDestinationId == R.id.splashFragment) {
            if (view != null && isAdded) {
                navController.navigate(destination)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
