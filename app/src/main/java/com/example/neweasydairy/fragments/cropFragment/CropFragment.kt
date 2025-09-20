package com.example.neweasydairy.fragments.cropFragment

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentCropBinding
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.fragments.noteFragment.imageFunctionality.ImageDataModelGallery
import com.example.neweasydairy.utilis.Objects.CHECK_NAVIGATION
import com.example.neweasydairy.utilis.Objects.CLICKEDITEMDATA
import com.example.neweasydairy.utilis.Objects.FROM_CROP_FRAGMENT
import com.example.neweasydairy.utilis.Objects.FROM_HOME_FRAGMENT
import com.example.neweasydairy.utilis.Objects.SEND_URI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

class CropFragment : Fragment() {
    private var _binding: FragmentCropBinding? = null
    private val binding get() = _binding
    private val TAG = "LifeCycle"
    var selectedImages: ArrayList<ImageDataModelGallery> = ArrayList()
    var fromWhichFragment = false
    var note: NotepadEntity? = null
    private var tagName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCropBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val argument = arguments?.getString(SEND_URI)
        //argument = arguments?.getString(CHECK_NAVIGATION).orEmpty()
        note = arguments?.getParcelable(CLICKEDITEMDATA)
        selectedImages = arguments?.getParcelableArrayList("selectedImages")!!
        fromWhichFragment = arguments?.getBoolean("isCreateFragment") == true
        tagName = arguments?.getString("tagName")
        Log.e(TAG, "onViewCreated: argument $argument")
        Log.e(TAG, "onViewCreated: select $selectedImages")
        Log.e("boolean", "onViewCreated: boolean $fromWhichFragment")
        binding?.cropImageView?.setImageUriAsync(argument?.toUri())
        clickListenerCropFragment()
        binding?.apply {

        }
    }

    private fun clickListenerCropFragment() {
        binding?.apply {
            btnRotate.setOnClickListener {
                cropImageView.rotateImage(90)
            }

            btnCancel.setOnClickListener {
                findNavController().navigateUp()
            }

            btnDone.setOnClickListener {
                val bitmap = cropImageView.getCroppedImage()
                val cropUri =
                    saveBitmapToUri(
                        bitmap = bitmap ?: return@setOnClickListener,
                        context = context ?: return@setOnClickListener
                    )
                selectedImages.add(ImageDataModelGallery(imagePath = cropUri.toString()))
                //.currentNoteId = note?.id
                val bundle = Bundle()
                bundle.putString(CHECK_NAVIGATION, FROM_HOME_FRAGMENT)
                bundle.putString(CHECK_NAVIGATION, FROM_CROP_FRAGMENT)
                bundle.putParcelable(CLICKEDITEMDATA, note)
                bundle.putStringArrayList(
                    "listOfAllTags",
                    arguments?.getStringArrayList("listOfAllTags")
                )
                bundle.putParcelableArrayList("selectedAllImages", selectedImages)
                when (fromWhichFragment) {

                    true -> {
                        tagName = arguments?.getString("tagName")
                        findNavController().navigate(
                            R.id.action_cropFragment_to_createNotesFragment,
                            bundle,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.cropFragment, true)
                                .build()
                        )
                    }

                    false -> {
                        //  findNavController().navigate(R.id.action_cropFragment_to_checkListFragment,bundle)
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun saveBitmapToUri(bitmap: Bitmap, context: Context): Uri? {
        val imageFile =
            File(context.cacheDir, "img_" + Calendar.getInstance().timeInMillis + ".jpg")

        Log.e("cropFragment", "saveBitmapToUri: $imageFile")

        try {
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            return Uri.fromFile(imageFile)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

}