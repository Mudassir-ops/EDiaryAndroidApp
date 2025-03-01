package com.example.neweasydairy.fragments.languageFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentLanguageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : Fragment() {
    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding
    private var adapter: LanguageAdapter? = null
    private val viewModel: LanguageViewModel by viewModels()


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
        adapter = LanguageAdapter(
            context = context ?: return,
            languageList = viewModel.getLanguageList(),
            onLanguageSelected = { isSelected ->
                Log.e("LanguageFragment", "onCreate: isSelected $isSelected")
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()
        observerViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun clickListener() {
        binding?.apply {
            btnDone.setOnClickListener {
                viewModel.setDoneValue(true)
                if (findNavController().currentDestination?.id == R.id.languageFragment) {
                    findNavController().navigate(R.id.action_languageFragment_to_introFragment)
                }
            }

        }
    }

    private fun observerViewModel() {
        viewModel.languageList.observe(viewLifecycleOwner) { languageList ->
            if (!languageList.isNullOrEmpty()) {
                adapter?.updateLanguageList(languageList)
            }
            if (binding?.languageRecyclerView?.adapter == null)
                binding?.languageRecyclerView?.adapter = adapter
        }
    }

}
