package com.example.neweasydairy.fragments.introFragment.introFragmentTwo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentIntroOneBinding
import com.example.easydiaryandjournalwithlock.databinding.FragmentIntroTwoBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageViewModel


class IntroFragmentTwo : Fragment() {
    private var _binding: FragmentIntroTwoBinding?=null
    private val binding get() = _binding
    private val languageViewModel: LanguageViewModel by activityViewModels()


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
        _binding = FragmentIntroTwoBinding.inflate(inflater,container,false)
        return  binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            btnNextIntroTwo.setOnClickListener {
               languageViewModel.setNextButtonIntroTwo(true)
                if (findNavController().currentDestination?.id == R.id.introFragmentTwo) {
                    findNavController().navigate(R.id.action_introFragmentTwo_to_permissionFragment)
                }
            }
            txtSkipTwo.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.introFragmentTwo) {
                    findNavController().navigate(R.id.action_introFragmentTwo_to_permissionFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}