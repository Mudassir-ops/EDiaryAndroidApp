package com.example.neweasydairy.fragments.nameFragment

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
    private val languageViewModel: LanguageViewModel by activityViewModels()

    fun Activity.setKeyboardVisibilityListener(onVisibilityChanged: (Boolean) -> Unit) {
        val contentView = findViewById<View>(android.R.id.content)
        contentView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            contentView.getWindowVisibleDisplayFrame(r)
            val screenHeight = contentView.rootView.height
            val keypadHeight = screenHeight - r.bottom
            onVisibilityChanged(keypadHeight > screenHeight * 0.15)
        }
    }


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

            activity?.setKeyboardVisibilityListener { isVisible ->
                if (isVisible) {
                    enableResize(true)
                } else {
                    enableResize(false)
                }
            }

            btnNext.setOnClickListener {
                val name = edTextName.text.toString()
                if (name.isEmpty()) {
                    activity?.toast("Please fill in your name")
                } else {
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
    fun enableResize(enable: Boolean) {
        val mode = if (enable) {
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        } else {
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        }
        activity?.window?.setSoftInputMode(mode)
    }


}