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
            homeViewModel.allNotes
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .filterIsInstance<NotesStates.AllNotes>()
                .collect { notesState ->
                    val notes = notesState.allNotes

                    // convert notes -> List<LibraryItem>
                    val items = notes.flatMap { note ->
                        val images = note.imageList.map { it.imagePath }
                        if (images.isNotEmpty()) {
                            listOf(
                                LibraryItem.DateItem(note.timestamp.toDateString()),
                                LibraryItem.ImagesItem(note.timestamp.toDateString(), images)
                            )
                        } else {
                            emptyList()
                        }
                    }
                    binding?.libraryRecyclerView?.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = MultiViewAdapter(items) { imagePath, date ->
                            val bundle = Bundle().apply {
                                putString("image_path", imagePath)
                                putString("date", date)
                            }
                            findNavController().navigate(R.id.imageViewFragment, bundle)
                            Log.d("ImageViewFragment", "Image send: $imagePath, date:$date")
                        }
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Long.toDateString(): String {
        val sdf = java.text.SimpleDateFormat("d MMMM, yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(this))
    }
}

