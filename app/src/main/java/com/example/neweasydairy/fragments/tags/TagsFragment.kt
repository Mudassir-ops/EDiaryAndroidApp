package com.example.neweasydairy.fragments.tags

import EditTagDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentTagsBinding
import com.example.neweasydairy.utilis.Objects.CHECK_NAVIGATION
import com.example.neweasydairy.utilis.Objects.DELETE_ACTION
import com.example.neweasydairy.utilis.Objects.EDIT_ACTION
import com.example.neweasydairy.utilis.Objects.FROM_HOME_FRAGMENT
import com.example.neweasydairy.utilis.Objects.FROM_TAG_FRAGMENT
import com.example.neweasydairy.utilis.Objects.ITEM_CLICK
import com.example.neweasydairy.utilis.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TagsFragment : Fragment() {
    private var _binding: FragmentTagsBinding? = null
    private val binding get() = _binding

    private var adapter: TagsAdapter? = null
    private val viewModel: TagsViewModel by viewModels()
    var editTagDialog:EditTagDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editTagDialog = EditTagDialog(activity = requireActivity()).apply {
            onUpdateTag = { updatedTag ->
                viewModel.updateCustomTagData(updatedTag)
            }
        }
        adapter = TagsAdapter(
            list = emptyList(),
            context = context?:return,
            onItemClick = { pair ->
                val tag = pair.first
                val action = pair.second
                when (action) {
                    DELETE_ACTION -> {
                        viewModel.deleteCustomTagById(tag.id)
                    }

                    EDIT_ACTION -> {
                        Log.e("tags", "onCreate: tags value ${pair.first.tagName}", )
                        editTagDialog?.setTagData(tag)
                        editTagDialog?.show()

                    }

                    ITEM_CLICK->{
                        val bundle = Bundle()
                        bundle.putString("tagName",pair.first.tagName)
                        bundle.putString(CHECK_NAVIGATION, FROM_TAG_FRAGMENT)
                        Log.e("itemClick", "onCreate: itemClick send ${pair.first.tagName}", )

                        if (findNavController().currentDestination?.id == R.id.tagsFragment) {
                            findNavController().navigate(R.id.action_tagsFragment_to_createNotesFragment,bundle)
                        }
                    }

                }
            }
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTagsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            tagsRecyclerView.adapter = adapter
        }
        setupEditTextListener()
        clickListener()
        observeViewModel()
    }

    private fun clickListener() {
        binding?.apply {
            icBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupEditTextListener() {
        binding?.edTags?.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                val inputText = textView.text.toString().trim()
                if (inputText.isNotEmpty()) {
                    viewModel.insertCustomTagData(inputText)
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textView.windowToken, 0)

                    textView.text = ""
                } else {
                   activity?.toast("Please enter some text")
                }
                true
            } else {
                false
            }
        }
    }


    private fun observeViewModel() {
        viewModel.allTags.observe(viewLifecycleOwner) { tags ->
            binding?.apply {
                if (tags.isNotEmpty()) {
                    txtNoData.visibility = View.GONE
                    tagsRecyclerView.visibility = View.VISIBLE
                    adapter?.updateTagList(tags)

                } else {
                    txtNoData.visibility = View.VISIBLE
                    tagsRecyclerView.visibility = View.GONE

                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



