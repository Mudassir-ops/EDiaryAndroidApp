package com.example.neweasydairy.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.neweasydairy.fragments.noteFragment.imageFunctionality.ImageDataModelGallery
import com.example.neweasydairy.typeConverter.ImageConverter
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "note_data_table")
data class NotepadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val noteTitle: String = "",
    val noteDescription: String = "",
    val color: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),

    @TypeConverters(ImageConverter::class)
    val imageList: List<ImageDataModelGallery> = emptyList(),

    val fontFamilyName: String = "normal",
    val icEmojiName: String = "happy",
    val txtHeadingName: Int? = null,
    val txtTextAlign: Int? = null,
    val textColorCode: Int? = null,
    val backgroundValue: Int? = null,
    val emojiRes: Int? = null,
    val bgImgRes: Int? = null,
    val emojiCardBgColor: String? = "",
    val emojiName: String? = "",
    val tagsList: List<CustomTagEntity>? = arrayListOf(),

    val txtHeadingSize: Float = 19F,
    val desHeadingSize: Float = 22F

) : Parcelable
