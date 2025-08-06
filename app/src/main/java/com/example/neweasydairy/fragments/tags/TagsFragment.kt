package com.example.neweasydairy.fragments.tags

import com.example.neweasydairy.dialogs.EditTagDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.databinding.FragmentTagsBinding
import com.example.neweasydairy.data.CustomTagEntity
import com.example.neweasydairy.utilis.Objects.DELETE_ACTION
import com.example.neweasydairy.utilis.Objects.EDIT_ACTION
import com.example.neweasydairy.utilis.Objects.ITEM_CLICK
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TagsFragment : Fragment() {
    private var _binding: FragmentTagsBinding? = null
    private val binding get() = _binding

    private var adapter: TagsAdapter? = null
    private val viewModel: TagsViewModel by viewModels()
    private var editTagDialog: EditTagDialog? = null
    private var selectedTagEntity: CustomTagEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editTagDialog = EditTagDialog(
            activity = requireActivity(),
            label1 = "Edit Tags",
            label2 = "Edit Tags",
            label3 = "Edit"
        ).apply {
            onUpdateTag = { updatedTag ->
                Log.e(
                    "setupEditTextListener",
                    "setupEditTextListener: --${updatedTag}"
                )
                viewModel.updateTag(
                    noteId = selectedTagEntity?.noteId ?: -1,
                    oldTag = selectedTagEntity,
                    newTag = updatedTag,
                )
                //  viewModel.updateCustomTagData(updatedTag)
            }
        }
        adapter = TagsAdapter(
            context = context ?: return,
            onItemClick = { pair ->
                selectedTagEntity = pair.first
                val tag = pair.first
                val action = pair.second
                when (action) {
                    DELETE_ACTION -> {
                        viewModel.updateTag(
                            noteId = selectedTagEntity?.noteId ?: -1,
                            oldTag = selectedTagEntity,
                            newTag = tag
                        )
                    }

                    EDIT_ACTION -> {
                        Log.e("tags", "onCreate: tags value ${pair.first.tagName}")
                        editTagDialog?.setTagData(tag)
                        editTagDialog?.show()
                    }

                    ITEM_CLICK -> {

//                        val bundle = Bundle()
//                        bundle.putString("tagName", pair.first.tagName)
//                        bundle.putString(CHECK_NAVIGATION, FROM_TAG_FRAGMENT)
//                        Log.e("itemClick", "onCreate: itemClick send ${pair.first.tagName}")
//
//                        if (findNavController().currentDestination?.id == R.id.tagsFragment) {
//                            findNavController().navigate(
//                                R.id.action_tagsFragment_to_createNotesFragment,
//                                bundle
//                            )
//                        }
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
        viewModel.getAllTags(viewLifecycleOwner.lifecycle)
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
        binding?.edTags?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                adapter?.filter(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        /*binding?.edTags?.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                val inputText = textView.text.toString().trim()
                Log.e("setupEditTextListener", "setupEditTextListener: $inputText--$inputText")
                if (inputText.isNotEmpty()) {
//                    val allTags =
//                        arrayListOf(selectedTagEntity?.copy(tagName = inputText))
//                    val updatedList = Gson().toJson(allTags)
//                    Log.e("setupEditTextListener", "setupEditTextListener: $allTags--$inputText")
//                    viewModel.updateTag(
//                        noteId = selectedTagEntity?.noteId ?: -1,
//                        tagsList = updatedList
//                    )
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textView.windowToken, 0)

                    textView.text = ""
                } else {
                    activity?.toast("Please enter some text")
                }
                true
            } else {
                false
            }
        }*/
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allTagsFlow.flowWithLifecycle(lifecycle).collect { tags ->
                binding?.apply {
                    if (tags.isNotEmpty()) {
                        txtNoData.visibility = View.GONE
                        tagsRecyclerView.visibility = View.VISIBLE
                        adapter?.setTags(tags)

                    } else {
                        txtNoData.visibility = View.VISIBLE
                        tagsRecyclerView.visibility = View.GONE

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



