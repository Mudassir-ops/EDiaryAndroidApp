package com.example.neweasydairy.fragments.libraryFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentLibraryBinding
import com.example.neweasydairy.fragments.homeFragment.HomeViewModel
import com.example.neweasydairy.fragments.homeFragment.NotesStates
import com.example.neweasydairy.fragments.imageViewFragment.ImageViewFragment
import com.example.neweasydairy.utilis.Objects.CHECK_NAVIGATION
import com.example.neweasydairy.utilis.Objects.FROM_CROP_FRAGMENT
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

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
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.allNotes.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .filterIsInstance<NotesStates.AllNotes>().collect { notes ->
                    binding?.libraryRecyclerView?.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding?.libraryRecyclerView?.adapter =
                        LibraryAdapter(notes.allNotes) { imagePath, date ->
                            val bundle = Bundle()
                            bundle.putString("image_path", imagePath)
                            bundle.putString("date", date)
                            findNavController().navigate(R.id.imageViewFragment, bundle)
                            Log.d("ImageViewFragment", "Image send: $imagePath, date:$date")
                        }
                }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}