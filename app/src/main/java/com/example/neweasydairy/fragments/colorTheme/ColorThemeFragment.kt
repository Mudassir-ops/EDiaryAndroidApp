package com.example.neweasydairy.fragments.colorTheme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.databinding.FragmentColorThemeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColorThemeFragment : Fragment() {
    private var _binding: FragmentColorThemeBinding? = null
    private val binding get() = _binding
    private var adapter: ColorThemeAdapter? = null
    private val viewModel: ColorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ColorThemeAdapter(context =activity?:return,
            colorThemeList = viewModel.getColorThemeList(),
            onColorThemeSelected = {isSelected ->
                Log.e("ColorThemeFragment", "onCreate: isSelected $isSelected")

            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentColorThemeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()
        observerViewModel()

    }

    private fun clickListener() {
        binding?.apply {
            icBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observerViewModel() {
        viewModel.colorThemeList.observe(viewLifecycleOwner) { colorThemeList ->
            if (!colorThemeList.isNullOrEmpty()) {
                adapter?.updateColorThemeList(colorThemeList)
            }
            if (binding?.colorThemeRecyclerView?.adapter == null)
                binding?.colorThemeRecyclerView?.adapter = adapter
        }
    }

}