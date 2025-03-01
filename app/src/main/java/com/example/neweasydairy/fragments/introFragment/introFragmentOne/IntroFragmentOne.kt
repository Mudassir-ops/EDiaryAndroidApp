package com.example.neweasydairy.fragments.introFragment.introFragmentOne

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentIntroOneBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragmentOne : Fragment() {
    private var _binding: FragmentIntroOneBinding?=null
    private val binding get() = _binding
    private val languageViewModel:LanguageViewModel by viewModels()

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
        _binding = FragmentIntroOneBinding.inflate(inflater,container,false)
        return  binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            btnNextIntroOne.setOnClickListener {
                languageViewModel.setNextButtonIntroOne(true)
                if (findNavController().currentDestination?.id == R.id.introFragment) {
                    findNavController().navigate(R.id.action_introFragment_to_introFragmentTwo)
                }
            }
            txtSkipOne.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.introFragment) {
                    findNavController().navigate(R.id.action_introFragment_to_introFragmentTwo)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}