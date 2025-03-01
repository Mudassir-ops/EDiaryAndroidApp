package com.example.neweasydairy.fragments.profileFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentProfileBinding
import com.example.easydiaryandjournalwithlock.databinding.FragmentSplashBinding
import com.example.neweasydairy.fragments.languageFragment.LanguageAdapter
import com.example.neweasydairy.fragments.languageFragment.LanguageViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding?=null
    private val binding get() = _binding
    private var adapter: ProfileAdapter? = null
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ProfileAdapter(
            context= context?:return,
            profileList =  viewModel.getProfileList(),
            onItemSelected =  { itemSelected->
                Log.e("profile", "onCreate: itemSelected ${itemSelected}", )

            }
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.profileList.observe(viewLifecycleOwner) { profileList ->
            if (!profileList.isNullOrEmpty()) {
                adapter?.updateProfileList(profileList)
            }
            if (binding?.drawerRecyclerView?.adapter == null)
                binding?.drawerRecyclerView?.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}