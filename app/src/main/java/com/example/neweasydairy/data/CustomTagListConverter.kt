package com.example.neweasydairy.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CustomTagListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromTagList(tags: List<CustomTagEntity>?): String {
        return gson.toJson(tags)
    }

    @TypeConverter
    fun toTagList(json: String): List<CustomTagEntity>? {
        val type = object : TypeToken<List<CustomTagEntity>>() {}.type
        return gson.fromJson(json, type)
    }
}
