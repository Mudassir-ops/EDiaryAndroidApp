package com.example.neweasydairy.fragments.noteFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.CustomTagEntity
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.fragments.noteFragment.imageFunctionality.ImageDataModelGallery
import com.example.neweasydairy.fragments.tags.TagsRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val createNoteRepository: CreateNoteRepository,
    private val tagsRepository: TagsRepository
) : ViewModel() {
    var currentNoteId: Int? = null
    val tagList = mutableListOf<String>()
    var title: String? = null
    var description: String? = null
    var icEmojiName: String? = null

    private val _backgroundState = MutableStateFlow(7)
    val backgroundState: StateFlow<Int> get() = _backgroundState

    private val _selectedImages = MutableLiveData<ArrayList<ImageDataModelGallery>>(ArrayList())
    val selectedImages: LiveData<ArrayList<ImageDataModelGallery>> = _selectedImages


    private val _tagName = MutableLiveData<String>()
    val tagName: LiveData<String> get() = _tagName

    fun setTagName(tag: String) {
        _tagName.value = tag
    }


    fun setBackgroundState(value: Int) {
        viewModelScope.launch {
            _backgroundState.emit(value)
        }
    }

    fun resetState() {
        viewModelScope.launch {
            _backgroundState.emit(7)
        }
    }

    fun insertNoteData(
        title: String,
        description: String,
        color: Int,
        imageFiles: ArrayList<ImageDataModelGallery>,
        timeStamp: Long,
        fontFamily: String,
        icEmojiName: String,
        txtHeadingName: Int,
        txtTextAlign: Int,
        txtColorCode: Int,
        backgroundValue: Int,
        emojiRes: Int,
        bgImgRes: Int,
        cardBgColor: String,
        emojiName: String,
        tagsList: List<String>,
        txtHeadingSize: Float = 19F,
        desHeadingSize: Float = 19F
    ) {
        viewModelScope.launch {
            val currentNote = NotepadEntity(
                noteTitle = title,
                noteDescription = description,
                color = color,
                imageList = imageFiles,
                timestamp = timeStamp,
                fontFamilyName = fontFamily,
                icEmojiName = icEmojiName,
                txtHeadingName = txtHeadingName,
                txtTextAlign = txtTextAlign,
                textColorCode = txtColorCode,
                backgroundValue = backgroundValue,
                emojiRes = emojiRes,
                bgImgRes = bgImgRes,
                emojiCardBgColor = cardBgColor,
                emojiName = emojiName,
                tagsList = listOf(),
                txtHeadingSize = txtHeadingSize,
                desHeadingSize = desHeadingSize
            )
            val noteId = createNoteRepository.insertNoteData(currentNote)
            if (tagsList.isNotEmpty()) {
                val allTags = tagsList.toEntity(noteId = noteId.toInt())
                createNoteRepository.updateTag(
                    noteId = noteId.toInt(),
                    tagsList = Gson().toJson(allTags)
                )
            }
        }
    }

    fun updateNoteData(
        id: Int,
        title: String,
        description: String,
        color: Int,
        imageFiles: ArrayList<ImageDataModelGallery>,
        timeStamp: Long,
        fontFamily: String,
        icEmojiName: String,
        txtHeadingName: Int,
        txtTextAlign: Int,
        txtColorCode: Int,
        backgroundValue: Int,
        emojiName: String,
        emojiRes: Int,
        bgImgRes: Int,
        cardBgColor: String,
        tagsList: List<String>,
        txtHeadingSize: Float = 19F,
        desHeadingSize: Float = 19F
    ) {
        viewModelScope.launch {
            val updatedNote = NotepadEntity(
                id = id,
                noteTitle = title,
                noteDescription = description,
                color = color,
                imageList = imageFiles,
                timestamp = timeStamp,
                fontFamilyName = fontFamily,
                icEmojiName = icEmojiName,
                txtHeadingName = txtHeadingName,
                txtTextAlign = txtTextAlign,
                textColorCode = txtColorCode,
                backgroundValue = backgroundValue,
                emojiName = emojiName,
                emojiRes = emojiRes,
                bgImgRes = bgImgRes,
                emojiCardBgColor = cardBgColor,
                tagsList = tagsList.toEntity(noteId = id),
                txtHeadingSize = txtHeadingSize,
                desHeadingSize = desHeadingSize
            )
            createNoteRepository.updateNoteData(noteEntity = updatedNote)
        }
    }


    fun updateImagesList(noteId: Int, imagePath: String) {
        viewModelScope.launch {
            createNoteRepository.updateImagesList(noteId, imagePath)
        }
    }
}
