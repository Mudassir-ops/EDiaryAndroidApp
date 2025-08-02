package com.example.neweasydairy.fragments.homeFragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentHomeBinding
import com.example.neweasydairy.dialogs.RatingDialog
import com.example.neweasydairy.fragments.noteFragment.CreateNoteViewModel
import com.example.neweasydairy.utilis.Objects.CHECK_NAVIGATION
import com.example.neweasydairy.utilis.Objects.CLICKEDITEMDATA
import com.example.neweasydairy.utilis.Objects.FROM_HOME_FRAGMENT
import com.example.neweasydairy.utilis.monthlyFormatDate
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding
    val homeViewModel: HomeViewModel by activityViewModels()
    val createNoteViewModel: CreateNoteViewModel by activityViewModels()
    lateinit var homeAdapter: HomeAdapter
    private var shimmerAdapter: ShimmerAdapter? = null
    var isAscending = true
    var currentRotation = 0f
    private var ratingDialog: RatingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratingDialog = RatingDialog(activity ?: return)
        homeAdapter = HomeAdapter(emptyList(),
            onItemClick = { note ->
                createNoteViewModel.currentNoteId = note.id
                val bundle = Bundle()
                bundle.putString(CHECK_NAVIGATION, FROM_HOME_FRAGMENT)
                bundle.putParcelable(CLICKEDITEMDATA, note)
                findNavController().navigate(R.id.createNotesFragment, bundle)
                Log.e("CheckItem", "Clicked Item id $note")
            },
            onItemLongClick = { note ->
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        homeViewModel.deleteNote(note)
                        val updatedList = homeAdapter.list.toMutableList()
                        updatedList.remove(note)
                        homeAdapter.updateList(updatedList)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.create().show()
            }
        )
        shimmerAdapter = ShimmerAdapter(itemCount = 6)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            val currentTimestamp = System.currentTimeMillis()
            val formattedDate = context?.monthlyFormatDate(currentTimestamp)
            txtDate.text = formattedDate
            shimmerAdapter = ShimmerAdapter(6)
            homeRecyclerView.adapter = shimmerAdapter
            clickListener(this@HomeFragment)
            observeViewModel()
        }
    }

    private fun observeViewModel() {
        homeViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            if (notes.isNotEmpty()) {
                binding?.groupHome?.visibility = View.GONE
                viewLifecycleOwner.lifecycleScope.launch {
                    val sortingOrder = homeViewModel.getSortingOrder()
                    val sortedNotes = if (sortingOrder?.sortingOrder == true) {
                        notes.sortedBy { it.timestamp }
                    } else {
                        notes.sortedByDescending { it.timestamp }
                    }
                    withContext(Main) {
                        homeAdapter.updateList(sortedNotes)
                        if (binding?.homeRecyclerView?.adapter !is HomeAdapter) {
                            binding?.homeRecyclerView?.adapter = homeAdapter
                        }
                        if (sortedNotes.size == 1 && !homeViewModel.isRatingDialogShown()) {
                            ratingDialog?.show()
                            homeViewModel.setRatingDialogShown()
                        }
                    }
                }
            } else {
                binding?.groupHome?.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}