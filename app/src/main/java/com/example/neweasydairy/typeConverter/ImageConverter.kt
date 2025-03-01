package com.example.neweasydairy.typeConverter

import androidx.room.TypeConverter
import com.example.neweasydairy.fragments.noteFragment.imageFunctionality.ImageDataModelGallery
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImageConverter {
    @TypeConverter
    fun fromRecordingDataList(imageList: List<ImageDataModelGallery>): String {
        val gson = Gson()
        return gson.toJson(imageList)
    }
    @TypeConverter
    fun toRecordingDataList(imageListString: String): List<ImageDataModelGallery> {
        val listType = object : TypeToken<List<ImageDataModelGallery>>() {}.type
        return Gson().fromJson(imageListString, listType)
    }

}