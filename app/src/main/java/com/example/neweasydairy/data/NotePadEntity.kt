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
    var noteDescription: String = "",
    var color: Int = 0,
    var timestamp: Long = System.currentTimeMillis(),
    @TypeConverters(ImageConverter::class)
    var imageList: List<ImageDataModelGallery> = emptyList(),
    var fontFamilyName:String = "normal",
    var icEmojiName:String = "happy",
    var txtHeadingName:Int = 0,
    var txtTextAlign:Int = 0,
    var textColorCode:Int = 0,
    var backgroundValue:Int = 0,
    var tagsText:String = ""
):Parcelable
