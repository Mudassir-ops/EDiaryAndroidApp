package com.example.neweasydairy.fragments.welcomeFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentChooseThemeBinding
import com.example.easydiaryandjournalwithlock.databinding.FragmentWelcomeBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageRepository
import com.example.neweasydairy.fragments.languageFragment.LanguageViewModel
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding
    private val languageViewModel: LanguageViewModel by activityViewModels()

    @Inject
    lateinit var languageRepository: LanguageRepository

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = languageRepository.getUserName()
        if (userName != null) {
            val formattedName = userName.replaceFirstChar { it.uppercaseChar() }
            binding?.txtWelcomeName?.text = getString(R.string.welcome_back, formattedName)
        }
        binding?.apply {


            btnContinue.setOnClickListener {
                languageViewModel.setWelcomeButtonWelcomeScreen(true)
                if (findNavController().currentDestination?.id == R.id.welcomeFragment) {
                    findNavController().navigate(R.id.action_welcomeFragment_to_mainFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}