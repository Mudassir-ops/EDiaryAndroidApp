package com.example.neweasydairy.fragments.noteFragment

import com.example.neweasydairy.dialogs.EditTagDialog
import android.Manifest
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentCreateNotesBinding
import com.example.neweasydairy.data.CustomTagEntity
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.dialogs.AudioDialog
import com.example.neweasydairy.dialogs.AudioPlayingDialog
import com.example.neweasydairy.dialogs.ChangeBackground
import com.example.neweasydairy.dialogs.FeelingDialog
import com.example.neweasydairy.dialogs.PhotoDialog
import com.example.neweasydairy.dialogs.SaveVoiceDialog
import com.example.neweasydairy.dialogs.TextDialog
import com.example.neweasydairy.fragments.noteFragment.imageFunctionality.ImageAdapter
import com.example.neweasydairy.fragments.noteFragment.imageFunctionality.ImageDataModelGallery
import com.example.neweasydairy.fragments.tags.TagsViewModel
import com.example.neweasydairy.interfaces.OnChangeBackgroundListener
import com.example.neweasydairy.interfaces.OnEmojiChangeListener
import com.example.neweasydairy.interfaces.OnFontSelectionListener
import com.example.neweasydairy.interfaces.OnTextAlignListener
import com.example.neweasydairy.interfaces.OnTextColorSelectionListener
import com.example.neweasydairy.interfaces.OnTextHeadingListener
import com.example.neweasydairy.utilis.AppEventLogger.logEventWithScope
import com.example.neweasydairy.utilis.Objects.CHECK_NAVIGATION
import com.example.neweasydairy.utilis.Objects.CLICKEDITEMDATA
import com.example.neweasydairy.utilis.Objects.FROM_CROP_FRAGMENT
import com.example.neweasydairy.utilis.Objects.FROM_HOME_FRAGMENT
import com.example.neweasydairy.utilis.Objects.FROM_ICON_ADD_NOTE
import com.example.neweasydairy.utilis.Objects.FROM_TAG_FRAGMENT
import com.example.neweasydairy.utilis.Objects.SEND_URI
import com.example.neweasydairy.utilis.addTags
import com.example.neweasydairy.utilis.monthlyFormatDate
import com.example.neweasydairy.utilis.saveToExternalStorage
import com.example.neweasydairy.utilis.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreateNotesFragment : Fragment(),
    OnTextAlignListener, OnTextHeadingListener, OnFontSelectionListener, OnChangeBackgroundListener,
    OnTextColorSelectionListener, OnEmojiChangeListener {

    var _binding: FragmentCreateNotesBinding? = null
    val binding get() = _binding
    var feelingDialogBinding: FeelingDialog? = null
    private var audioDialog: AudioDialog? = null
    private var audioPlayingDialogBinding: AudioPlayingDialog? = null
    private var saveVoiceDialog: SaveVoiceDialog? = null
    var textDialog: TextDialog? = null
    private lateinit var imageAdapter: ImageAdapter
    private var argument = ""
    var backgroundDialog: ChangeBackground? = null
    var photoDialog: PhotoDialog? = null
    private lateinit var requestCameraPermission: ActivityResultLauncher<String>
    private lateinit var requestGalleryPermission: ActivityResultLauncher<String>

    private lateinit var takePicturePreviewLauncher: ActivityResultLauncher<Void?>
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    var selectedImages: ArrayList<ImageDataModelGallery> = ArrayList()
    val viewModel: CreateNoteViewModel by activityViewModels()
    private val tagsViewModel: TagsViewModel by activityViewModels()
    var backgroundValue: Int = 7
    private var cameraPermissionDeniedCount = 0
    private var galleryPermissionDeniedCount = 0
    var selectedFontFamily: String = "Margarine"
    var note: NotepadEntity? = null
    private var tagName: String? = null
    var editTagDialog: EditTagDialog? = null
    val listOfAllTags = arrayListOf("")
    var textHeadingAndDescriptionSize: Pair<Float, Float>? = Pair(19F, 22F)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_createNotesFragment_to_mainFragment)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateNotesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDialogs()
        setupPermissions()
        setupImagePickers()
        editTagDialog = EditTagDialog(
            activity = activity ?: return,
            label1 = "Add New Tag",
            label2 = "Add Tag",
            label3 = "Add", onUpdateTag = {
                Log.e("updatedText", "onCreate: $it")
                editTagDialog?.dismiss()
                val defaultColor =
                    ContextCompat.getColor(context ?: return@EditTagDialog, R.color.ic_color)
                binding?.icHash?.setColorFilter(defaultColor)
                tagsViewModel.insertLocalTag(tag = it.tagName)
                viewLifecycleOwner.lifecycleScope.logEventWithScope(
                    name = "Create_Note_New_Tag_Dialog_click",
                    params = emptyMap()
                )
            }, onCancelTag = {
                viewLifecycleOwner.lifecycleScope.logEventWithScope(
                    name = "Create_Note_Cancel_Tag_click",
                    params = emptyMap()
                )
                val defaultColor =
                    ContextCompat.getColor(context ?: return@EditTagDialog, R.color.ic_color)
                binding?.icHash?.setColorFilter(defaultColor)
            }
        )
        imageAdapter = ImageAdapter(
            imageList = selectedImages,
            context ?: return,
            onShareClick = { imagePath ->
                viewLifecycleOwner.lifecycleScope.logEventWithScope(
                    name = "Create_Note_Screen_Share_click",
                    params = emptyMap()
                )
                //  imagePath.shareImage(requireContext())
                Log.e("imageUriSaqib", "onShareClick:, imagePath: $imagePath")
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        argument = arguments?.getString(CHECK_NAVIGATION).orEmpty()
        note = arguments?.getParcelable(CLICKEDITEMDATA)
        Log.d("note", "onViewCreated: ${note?.id}--${note?.fontFamilyName}")
        selectedImages = arguments?.getParcelableArrayList("selectedAllImages") ?: arrayListOf()
        tagName = arguments?.getString("tagName")
        selectedFontFamily = note?.fontFamilyName ?: selectedFontFamily
        handleNavigationArguments()
        observeBackgroundState()
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetState()
        tagsViewModel.clearLocalTags()
        _binding = null
    }

    override fun onChangeBackground(changeBackground: Int) {
        viewLifecycleOwner.lifecycleScope.logEventWithScope(
            name = "Create_Note_onChangeBackground",
            params = emptyMap()
        )
        binding?.backgroundImage?.setBackgroundByIndex(context, changeBackground)
    }

    override fun onTextAlignChanged(textAlignment: Int) {
        viewLifecycleOwner.lifecycleScope.logEventWithScope(
            name = "Create_Note_onTextAlignChanged",
            params = emptyMap()
        )
        binding?.apply {
            txtTitle.setTextAlignmentByIndex(textAlignment)
            txtEdDescription.setTextAlignmentByIndex(textAlignment)
        }
    }

    override fun onTextHeadingChanged(textHeading: Int) {
        viewLifecycleOwner.lifecycleScope.logEventWithScope(
            name = "Create_Note_onTextHeadingChanged",
            params = emptyMap()
        )
        binding?.apply {
            textHeadingAndDescriptionSize =
                Pair((getHeadingSize(textHeading) + 0F), (getHeadingSize(textHeading) + 3F))
            txtTitle.setHeadingSize(textHeadingAndDescriptionSize?.first ?: 19F)
            txtEdDescription.setHeadingSize(textHeadingAndDescriptionSize?.second ?: 22F)
        }
    }

    override fun onFontSelected(fontName: String) {
        viewLifecycleOwner.lifecycleScope.logEventWithScope(
            name = "Create_Note_onFontSelected",
            params = emptyMap()
        )
        binding?.apply {
            selectedFontFamily = fontName
            txtTitle.setFont(fontName, context ?: return)
            txtEdDescription.setFont(fontName, context ?: return)
        }
    }

    override fun onTextColorSelected(color: Int) {
        viewLifecycleOwner.lifecycleScope.logEventWithScope(
            name = "Create_Note_onTextColorSelected",
            params = emptyMap()
        )
        binding?.apply {
            txtTitle.setTextColor(color)
            txtEdDescription.setTextColor(color)
        }
    }

    override fun onEmojiSelected(emojiName: String) {
        viewLifecycleOwner.lifecycleScope.logEventWithScope(
            name = "Create_Note_onEmojiSelected",
            params = emptyMap()
        )
        binding?.icEmoji?.setEmoji(emojiName, context)
    }

    private fun handleNavigationArguments() {
        when (argument) {
            FROM_CROP_FRAGMENT -> binding?.apply {
                txtTitle.setText(viewModel.title)
                txtEdDescription.setText(viewModel.description)
                viewModel.icEmojiName?.let { binding?.icEmoji?.setEmoji(it, context).toString() }
            }

            FROM_HOME_FRAGMENT -> {
                Log.d("saqibRehman", "Current TagName: FROM_HOME_FRAGMENT")
                note?.let { setupNoteData(it) }
            }

            FROM_TAG_FRAGMENT -> binding?.apply {
                txtTitle.setText(viewModel.title)
                txtEdDescription.setText(viewModel.description)
                viewModel.icEmojiName?.let { binding?.icEmoji?.setEmoji(it, context).toString() }
                viewModel.selectedImages.observe(viewLifecycleOwner) { images ->
                    selectedImages.clear()
                    selectedImages.addAll(images)
                    Log.d("selectedImages", "observe Selected Images: $selectedImages")
                    binding?.imageRecyclerView?.adapter = imageAdapter
                    imageAdapter.updateImageList(selectedImages)
                    selectedImages.reverse()
                }
            }

            FROM_ICON_ADD_NOTE -> resetNoteUI()
        }
    }

    private fun setupNoteData(note: NotepadEntity) {
        binding?.apply {
            viewModel.currentNoteId = note.id
            txtTitle.setText(note.noteTitle)
            txtEdDescription.setText(note.noteDescription)
            binding?.icEmoji?.setEmoji(note.icEmojiName, context)
            selectedImages = ArrayList(note.imageList)
            imageAdapter.updateImageList(selectedImages)
            setupTags()
            setupTextAlignment(note.txtTextAlign)
            setupBackground(note.backgroundValue)
            setupFont(note.fontFamilyName)
            setupTextSize(note.txtHeadingName)
        }
    }

    private fun setupTags() {
        Log.e("setupTags-->", "setupTags: ${viewModel.currentNoteId}")
        if (viewModel.currentNoteId != null && note?.tagsList?.isNotEmpty() == true) {
            Log.e("setupTags-->", "setupTags: ${viewModel.currentNoteId}--${note?.tagsList}")
            tagsViewModel.addAllTagsForCreatedNote(allTags = note?.tagsList?.toDomain() ?: listOf())
        } else {
            tagsViewModel.addAllTagsForCreatedNote(allTags = arrayListOf("Unknown"))
        }
        viewLifecycleOwner.lifecycleScope.launch {
            tagsViewModel.tagsStateFlow.flowWithLifecycle(lifecycle).collect {
                val rawTags = it.map { tag -> tag }
                val tagNames = if (rawTags.size > 1 && rawTags.contains("Unknown")) {
                    rawTags.filter { tag -> tag != "Unknown" }.toMutableList()
                } else {
                    rawTags.toMutableList()
                }
                listOfAllTags.clear()
                listOfAllTags.addAll(tagNames)
                binding?.flexboxLayout?.apply {
                    removeAllViews()
                    if (tagNames.isNotEmpty()) {
                        visibility = View.VISIBLE
                        addTags(tagNames, onTagClick = {
                            editTagDialog?.show()
                        }, onRemoveTagClick = { tag ->
                            tagsViewModel.removeTag(tag)
                        })
                    } else {
                        visibility = View.GONE
                        viewModel.tagList.clear()
                    }
                }
            }

        }
    }

    private fun setupTextAlignment(alignment: Int?) {
        binding?.apply {
            val gravity = when (alignment) {
                0 -> Gravity.START
                1 -> Gravity.CENTER
                2 -> Gravity.END
                else -> Gravity.START
            }
            txtTitle.gravity = gravity
            txtEdDescription.gravity = gravity
        }
    }

    private fun setupBackground(backgroundValue: Int?) {
        Log.e("setupBackground", "setupBackground:$backgroundValue ")
        val drawableRes = when (backgroundValue) {
            0 -> R.drawable.color_theme_one
            1 -> R.drawable.background_1
            2 -> R.drawable.background_2
            3 -> R.drawable.background_3
            4 -> R.drawable.background_4
            5 -> R.drawable.background_5
            else -> R.drawable.bg_add_note
        }

        binding?.backgroundImage?.setImageDrawable(
            ContextCompat.getDrawable(
                context ?: return,
                drawableRes
            )
        )

    }

    private fun setupFont(fontName: String?) {
        val fontRes = when (fontName) {
            "Intaliana" -> R.font.italiana_regular
            "Leckerli" -> R.font.leckerlione_regular
            "Margarine" -> R.font.margarine_regular
            "Rethink" -> R.font.rethinksans_regular
            "Pacifico" -> R.font.pacifico
            "Lobster" -> R.font.lobster_regular
            else -> null
        }
        fontRes?.let { binding?.txtTitle?.typeface = ResourcesCompat.getFont(requireContext(), it) }
        fontRes?.let {
            binding?.txtEdDescription?.typeface = ResourcesCompat.getFont(requireContext(), it)
        }
    }

    private fun setupTextSize(size: Int?) {
        val (titleSize, descriptionSize) = when (size) {
            34 -> 17f to 14f
            40 -> 20f to 17f
            46 -> 23f to 20f
            else -> 17f to 14f
        }
        binding?.apply {
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize)
            txtEdDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, descriptionSize)
        }
    }

    private fun resetNoteUI() {
        binding?.apply {
            viewModel.tagList.clear()
            flexboxLayout.removeAllViews()
            backgroundImage.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_add_note
                )
            )
            binding?.flexboxLayout?.addTags(arrayListOf("Unknown"), onTagClick = {
                editTagDialog?.show()
            }) {
                tagsViewModel.removeTag(it)
            }
        }
    }

    private fun observeBackgroundState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.backgroundState.flowWithLifecycle(lifecycle).collect { backgroundVal ->
                backgroundValue = backgroundVal
                if (backgroundVal != 7) {
                    setupBackground(backgroundValue)
                }
            }
        }
    }

    private fun setupUI() {
        binding?.apply {
            val formattedDate = requireContext().monthlyFormatDate(System.currentTimeMillis())
            txtDate.text = formattedDate
            imageRecyclerView.adapter = imageAdapter
            imageAdapter.updateImageList(selectedImages)
            selectedImages.reverse()
            clickListener(requireContext(), this@CreateNotesFragment)
            viewModel.currentNoteId?.let {

            } ?: run {
                txtTitle.setSelection(txtTitle.text?.length ?: 0)
                txtTitle.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(txtTitle, InputMethodManager.SHOW_IMPLICIT)
            }

            try {
                binding?.apply {
                    textHeadingAndDescriptionSize =
                        Pair(note?.txtHeadingSize ?: 19F, note?.desHeadingSize ?: 19F)
                    txtTitle.setHeadingSize(textHeadingAndDescriptionSize?.first ?: 19F)
                    txtEdDescription.setHeadingSize(textHeadingAndDescriptionSize?.second ?: 19F)
                }
                Log.e("setupUI", "setupUI:${note?.textColorCode} ")
                val color =
                    note?.textColorCode ?: ContextCompat.getColor(
                        context ?: return,
                        R.color.black
                    )
                binding?.txtTitle?.setTextColor(color)
                binding?.txtEdDescription?.setTextColor(color)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            setupTags()
        }
    }

    private fun setupDialogs() {
        feelingDialogBinding = FeelingDialog(activity ?: return).apply {
            setOnEmojiSelectionListener(this@CreateNotesFragment)
        }
        audioDialog = AudioDialog(activity ?: return)
        audioPlayingDialogBinding = AudioPlayingDialog(activity ?: return)
        saveVoiceDialog = SaveVoiceDialog(activity ?: return)
        textDialog = TextDialog(activity ?: return).apply {
            setOnTextAlignListener(this@CreateNotesFragment)
            setOnTextHeadingListener(this@CreateNotesFragment)
            setFontSelectionListener(this@CreateNotesFragment)
            setOnTextColorSelectionListener(this@CreateNotesFragment)
        }
        backgroundDialog = ChangeBackground(activity ?: return, viewModel).apply {
            setonChangeBackground(this@CreateNotesFragment)
        }
        setupPhotoDialog()
    }

    private fun setupPermissions() {
        requestCameraPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                handleCameraPermissionResult(isGranted)
            }

        requestGalleryPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                handleGalleryPermissionResult(isGranted)
            }
    }

    private fun handleCameraPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            context?.getSharedPreferences("AppPrefs", MODE_PRIVATE)?.edit() {
                putBoolean("isComingFromCamera", true)
            }
            viewLifecycleOwner.lifecycleScope.logEventWithScope(
                name = "Create_Note_Camera_Picker",
                params = emptyMap()
            )
            takePicturePreviewLauncher.launch(null)
            cameraPermissionDeniedCount = 0
        } else {
            cameraPermissionDeniedCount++
            if (cameraPermissionDeniedCount >= 2) {
                showPermissionDialog(context ?: return, this)
            }
        }
        photoDialog?.dismiss()
    }

    private fun handleGalleryPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            context?.getSharedPreferences("AppPrefs", MODE_PRIVATE)?.edit {
                putBoolean("isComingFromGallery", true)
            }
            viewLifecycleOwner.lifecycleScope.logEventWithScope(
                name = "Create_Note_Image_Picker",
                params = emptyMap()
            )
            galleryPermissionDeniedCount = 0
            pickImageLauncher.launch("image/*")
        } else {
            galleryPermissionDeniedCount++
            if (galleryPermissionDeniedCount >= 2) {
                showPermissionDialog(context ?: return, this)
            }
        }
    }

    private fun setupPhotoDialog() {
        photoDialog = PhotoDialog(
            activity ?: return,
            cameraCallBack = {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            },

            galleryCallBack = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestGalleryPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    requestGalleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        )
    }

    private fun setupImagePickers() {
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                tagName?.let { it1 -> viewModel.setTagName(it1) }
                navigateToCropFragment(it.toString())
            }
        }

        takePicturePreviewLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                tagName?.let { it1 -> viewModel.setTagName(it1) }
                bitmap?.let {
                    processCapturedImage(it)
                }
            }
    }

    private fun navigateToCropFragment(imageUri: String) {
        if (findNavController().currentDestination?.id == R.id.createNotesFragment) {
            viewModel.currentNoteId = note?.id
            val bundle = Bundle().apply {
                putString(SEND_URI, imageUri)
                putBoolean("isCreateFragment", true)
                putString(CHECK_NAVIGATION, FROM_HOME_FRAGMENT)
                putParcelable(CLICKEDITEMDATA, note)
                putParcelableArrayList("selectedImages", selectedImages)
            }
            findNavController().navigate(R.id.action_createNotesFragment_to_cropFragment, bundle)
        }
    }

    private fun processCapturedImage(bitmap: Bitmap) {
        val currentDateTime =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Image_$currentDateTime"
        val uri = bitmap.saveToExternalStorage(requireContext(), fileName)

        uri?.let { savedUri ->
            navigateToCropFragment(savedUri.toString())
        } ?: run {
            activity?.toast("Failed to save Image")
        }
    }

    private fun List<CustomTagEntity>.toDomain(): List<String> {
        return map {
            it.tagName
        }
    }
}