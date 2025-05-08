package com.example.neweasydairy.fragments.homeFragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentHomeBinding
import com.example.easydiaryandjournalwithlock.databinding.FragmentPermissionBinding
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.dialogs.AudioDialog
import com.example.neweasydairy.dialogs.RatingDialog
import com.example.neweasydairy.utilis.Objects.CHECK_NAVIGATION
import com.example.neweasydairy.utilis.Objects.CLICKEDITEMDATA
import com.example.neweasydairy.utilis.Objects.FROM_HOME_FRAGMENT
import com.example.neweasydairy.utilis.formatDate
import com.example.neweasydairy.utilis.monthlyFormatDate


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding
    val homeViewModel: HomeViewModel by activityViewModels()
    lateinit var homeAdapter: HomeAdapter
    var isAscending = true
    var currentRotation = 0f
    private var ratingDialog:RatingDialog?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratingDialog = RatingDialog(activity?:return)
        homeAdapter = HomeAdapter(emptyList(),
            onItemClick = { note->
            val bundle = Bundle()
            bundle.putString(CHECK_NAVIGATION,FROM_HOME_FRAGMENT)
            bundle.putParcelable(CLICKEDITEMDATA,note)
            findNavController().navigate(R.id.createNotesFragment,bundle)
            Log.e("CheckItem", "Clicked Item id $note")
        },
            onItemLongClick = {note->
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
            homeRecyclerView.adapter = homeAdapter
            clickListener(this@HomeFragment)
            observeViewModel()
        }

    }

    private fun observeViewModel() {
            homeViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
                if (notes.isNotEmpty()) {
                    binding?.groupHome?.visibility=View.GONE
                    homeAdapter.updateList(notes)
                    binding?.homeRecyclerView?.adapter = homeAdapter
                    if (notes.size == 1 && !homeViewModel.isRatingDialogShown()) {
                        ratingDialog?.show()
                        homeViewModel.setRatingDialogShown()
                    }
                } else {
                    binding?.groupHome?.visibility=View.VISIBLE
                }
            }
    }

    private fun showDeleteConfirmationDialog(note: NotepadEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { dialog, id ->
                homeViewModel.deleteNote(note)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}