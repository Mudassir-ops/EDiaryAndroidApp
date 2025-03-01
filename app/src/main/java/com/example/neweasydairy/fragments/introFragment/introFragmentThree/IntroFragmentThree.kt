package com.example.neweasydairy.fragments.introFragment.introFragmentThree

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
import com.example.easydiaryandjournalwithlock.databinding.FragmentIntroThreeBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageViewModel


class IntroFragmentThree : Fragment() {
    private var _binding: FragmentIntroThreeBinding?=null
    private val binding get() = _binding
    private val languageViewModel:LanguageViewModel by activityViewModels()


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
        _binding = FragmentIntroThreeBinding.inflate(inflater,container,false)
        return  binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            btnNextIntroThree.setOnClickListener {
                languageViewModel.setNextButtonIntroThree(true)
                if (findNavController().currentDestination?.id == R.id.introFragmentThree){
                    findNavController().navigate(R.id.action_introFragmentThree_to_permissionFragment)
                }
            }
            txtSkipthree.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.introFragmentThree){
                    findNavController().navigate(R.id.action_introFragmentThree_to_permissionFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}