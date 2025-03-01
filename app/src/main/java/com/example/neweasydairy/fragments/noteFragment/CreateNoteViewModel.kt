package com.example.neweasydairy.fragments.noteFragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.NotepadEntity
import com.example.neweasydairy.fragments.noteFragment.imageFunctionality.ImageDataModelGallery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val application: Application,
    private val createNoteRepository: CreateNoteRepository,
) : ViewModel() {
    val tagList = mutableListOf<String>()
    var title: String? = null
    var description: String? = null
    var icEmojiName:String? = null
    private val _backgroundState = MutableLiveData<Int>()
    val backgroundState: LiveData<Int> get() = _backgroundState

    private val _selectedImages = MutableLiveData<ArrayList<ImageDataModelGallery>>(ArrayList())
    val selectedImages: LiveData<ArrayList<ImageDataModelGallery>> = _selectedImages

    fun setSelectedImages(images: ArrayList<ImageDataModelGallery>) {
        _selectedImages.value = images
    }


    private val _tagName = MutableLiveData<String>()
    val tagName: LiveData<String> get() = _tagName

    fun setTagName(tag: String) {
        _tagName.value = tag
    }


    fun setBackgroundState(value: Int) {
        _backgroundState.value = value
    }

    fun insertNoteData(
        title: String,
        description: String,
        color: Int,
        imageFiles : ArrayList<ImageDataModelGallery>,
        timeStamp : Long,
        fontFamily:String,
        icEmojiName:String,
        txtHeadingName:Int,
        txtTextAlign:Int,
        txtColorCode:Int,
        backgroundValue:Int,
        tagsText:String
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
                tagsText = tagsText)

            createNoteRepository.insertNoteData(noteEntity)
        }
    }

}
