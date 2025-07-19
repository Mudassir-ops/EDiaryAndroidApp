package com.example.neweasydairy.fragments.noteFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.fragments.noteFragment.imageFunctionality.ImageDataModelGallery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val createNoteRepository: CreateNoteRepository,
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
        tagsText: String,
        emojiRes: Int,
        bgImgRes: Int,
        cardBgColor: String,
        emojiName: String

    ) {
        viewModelScope.launch {
            val noteEntity = NotepadEntity(
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
                tagsText = tagsText,
                emojiRes = emojiRes,
                bgImgRes = bgImgRes,
                emojiCardBgColor = cardBgColor,
                emojiName = emojiName
            )

            createNoteRepository.insertNoteData(noteEntity)
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
        tagsText: String
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
                tagsText = tagsText
            )
            createNoteRepository.updateNoteData(updatedNote)
        }
    }

}
