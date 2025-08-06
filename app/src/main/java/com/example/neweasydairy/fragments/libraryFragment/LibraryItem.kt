package com.example.neweasydairy.fragments.libraryFragment

sealed class LibraryItem {
    data class DateItem(val date: String) : LibraryItem()
    data class ImagesItem(val date: String, val imagePaths: List<String>) : LibraryItem()
}
