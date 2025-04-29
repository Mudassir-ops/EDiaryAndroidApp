package com.example.neweasydairy.fragments.imageViewFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentImageViewBinding
import com.example.easydiaryandjournalwithlock.databinding.FragmentLibraryBinding


class ImageViewFragment : Fragment() {
    private var _binding: FragmentImageViewBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageViewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imagePath = arguments?.getString("image_path")
        val date = arguments?.getString("date")
        Log.d("ImageViewFragment", "Received imagePath: $imagePath,date:$date")
        binding?.apply {
            txtDate.text = date
            icBack.setOnClickListener{
                findNavController().navigateUp()
            }

            imagePath?.let {
                Glide.with(context?:return)
                    .load(it)
                    .into(imageView!!)
            }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}