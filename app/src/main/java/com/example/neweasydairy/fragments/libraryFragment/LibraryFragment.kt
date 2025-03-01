package com.example.neweasydairy.fragments.libraryFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentLibraryBinding
import com.example.neweasydairy.fragments.homeFragment.HomeViewModel

class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            binding?.libraryRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
            binding?.libraryRecyclerView?.adapter = LibraryAdapter(notes)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}