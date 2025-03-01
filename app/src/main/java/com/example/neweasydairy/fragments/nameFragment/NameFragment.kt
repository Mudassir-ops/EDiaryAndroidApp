package com.example.neweasydairy.fragments.nameFragment

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
import com.example.easydiaryandjournalwithlock.databinding.FragmentNameBinding
import com.example.easydiaryandjournalwithlock.databinding.FragmentPermissionBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageViewModel
import com.example.neweasydairy.utilis.toast


class NameFragment : Fragment() {
    private var _binding: FragmentNameBinding? = null
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
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            btnNext.setOnClickListener {
                val name = edTextName.text.toString()
                if (name.isEmpty()){
                  activity?.toast("Please fill in your name")
                }else {
                    languageViewModel.setUserName(name)
                    languageViewModel.setNextButtonNameScreen(true)
                    if (findNavController().currentDestination?.id == R.id.nameFragment) {
                        findNavController().navigate(R.id.action_nameFragment_to_pinFragment)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}