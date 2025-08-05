package com.example.neweasydairy.fragments.noteFragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentCreateNotesBinding
import com.example.neweasydairy.data.CustomTagEntity
import com.example.neweasydairy.utilis.AppEventLogger.logEventWithScope
import com.example.neweasydairy.utilis.Objects.FROM_ICON_ADD_NOTE
import com.example.neweasydairy.utilis.toast
import java.util.Date

fun FragmentCreateNotesBinding?.clickListener(context: Context, fragment: CreateNotesFragment) {
    this?.apply {
        val icons = listOf(icGrid, icText, icImageNote, icHash)
        icHash.setOnClickListener {
            fragment.viewLifecycleOwner.lifecycleScope.logEventWithScope(
                name = "Create_Note_Add_Tag_click",
                params = emptyMap()
            )
            icHash.setColorFilter(ContextCompat.getColor(context, R.color.app_color))
            val isNewNote = fragment.viewModel.currentNoteId == null
            val hasNoTags = fragment.note?.tagsList.isNullOrEmpty()
            val hasUnknownTag =
                fragment.note?.tagsList?.map { it.tagName }?.contains("Unknown") == true
            if (isNewNote || hasNoTags || hasUnknownTag) {
                fragment.editTagDialog?.show()
            } else {
                fragment.editTagDialog?.show()
//                if (fragment.findNavController().currentDestination?.id == R.id.createNotesFragment) {
//                    fragment.findNavController()
//                        .navigate(R.id.action_createNotesFragment_to_tagsFragment)
//                }
            }
            viewTag.visibility = View.GONE
            Log.d("selectedImages", "Before navigating: ${fragment.selectedImages}")
        }

        icBack.setOnClickListener {
            fragment.findNavController().navigateUp()
        }
        icEmoji.setOnClickListener {
            fragment.viewLifecycleOwner.lifecycleScope.logEventWithScope(
                name = "Create_Note_Emoji_click",
                params = emptyMap()
            )
            fragment.feelingDialogBinding?.show()
        }
        icGrid.setOnClickListener {
            fragment.viewLifecycleOwner.lifecycleScope.logEventWithScope(
                name = "Create_Note_Bg_Grid_click",
                params = emptyMap()
            )
            resetIconColors(context, icons)
            icGrid.setColorFilter(ContextCompat.getColor(context, R.color.app_color))
            fragment.backgroundDialog?.show()
        }
        icImageNote.setOnClickListener {
            fragment.viewLifecycleOwner.lifecycleScope.logEventWithScope(
                name = "Create_Note_Image_FROM_click",
                params = emptyMap()
            )
            fragment.viewModel.title = fragment.binding?.txtTitle?.text.toString()
            fragment.viewModel.description = fragment.binding?.txtEdDescription?.text.toString()
            fragment.viewModel.icEmojiName =
                fragment.binding?.icEmoji?.contentDescription.toString()
            resetIconColors(context, icons)
            icImageNote.setColorFilter(ContextCompat.getColor(context, R.color.app_color))
            fragment.photoDialog?.show()
        }
        icText.setOnClickListener {
            fragment.viewLifecycleOwner.lifecycleScope.logEventWithScope(
                name = "Create_Note_Text_click",
                params = emptyMap()
            )
            resetIconColors(context, icons)
            icText.setColorFilter(ContextCompat.getColor(context, R.color.app_color))
            fragment.textDialog?.show()
        }
        txtSave.setOnClickListener {
            fragment.viewLifecycleOwner.lifecycleScope.logEventWithScope(
                name = "Create_Note_Save_clicked",
                params = emptyMap()
            )
            insertData(notesFragment = fragment)
            fragment.activity?.toast("Data Save Successfully")
            if (fragment.findNavController().currentDestination?.id == R.id.createNotesFragment) {
                fragment.findNavController()
                    .navigate(R.id.action_createNotesFragment_to_mainFragment)
            }
        }
    }
}

private fun resetIconColors(context: Context, icons: List<AppCompatImageView>) {
    val defaultColor = ContextCompat.getColor(context, R.color.ic_color)
    icons.forEach { it.setColorFilter(defaultColor) }
}

fun showPermissionDialog(context: Context, fragment: Fragment) {
    val builder = AlertDialog.Builder(fragment.requireActivity())
    val dialog = builder.setTitle("Permission Required")
        .setMessage("Required permissions have been set to 'Don't ask again'. Please enable them in settings.")
        .setCancelable(true)
        .setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        .setPositiveButton("Settings") { dialogInterface, _ ->
            redirectToSystemSettings(context = context)
            dialogInterface.dismiss()
        }
        .create()

    dialog.setOnShowListener {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            redirectToSystemSettings(context)
            dialog.dismiss()
        }
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            dialog.dismiss()
        }
    }
    dialog.show()
}

private fun redirectToSystemSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    context.startActivity(intent)
}

fun insertData(notesFragment: CreateNotesFragment) {
    val description = notesFragment.binding?.txtEdDescription?.text.toString()
    val title = notesFragment.binding?.txtTitle?.text.toString()
    val currentTime = Date()
    val emojiName = notesFragment.binding?.icEmoji?.contentDescription
    val textAlignment: Int = when (notesFragment.binding?.txtTitle?.gravity) {
        8388661 -> {
            2
        }

        17 -> {
            1
        }

        else -> {
            0
        }
    }
    val textColor = notesFragment.binding?.txtTitle?.currentTextColor ?: 0
    Log.e("selectedFontFamily-->", "insertData: ${notesFragment.selectedFontFamily}")
    notesFragment.binding?.apply {
        val noteId = notesFragment.viewModel.currentNoteId
        if (noteId != null) {
            notesFragment.viewModel.updateNoteData(
                id = noteId,
                title = title,
                description = description,
                color = notesFragment.backgroundValue,
                imageFiles = notesFragment.selectedImages,
                timeStamp = notesFragment.note?.timestamp ?: currentTime.time,
                fontFamily = notesFragment.selectedFontFamily,
                icEmojiName = emojiName.toString(),
                txtHeadingName = 18,
                txtTextAlign = textAlignment,
                txtColorCode = textColor,
                emojiName = emojiName.toString().getEmojiName(),
                backgroundValue = notesFragment.backgroundValue,
                emojiRes = getEmoji(emojiName.toString()),
                bgImgRes = emojiName.toString().getEmojiColorForCardBg(),
                cardBgColor = getEmojiColor(emojiName.toString()),
                tagsList = notesFragment.listOfAllTags,
                txtHeadingSize = notesFragment.textHeadingAndDescriptionSize?.first ?: 19F,
                desHeadingSize = notesFragment.textHeadingAndDescriptionSize?.second ?: 22F
            )
        } else {
            notesFragment.viewModel.insertNoteData(
                title = title,
                description = description,
                color = notesFragment.backgroundValue,
                imageFiles = notesFragment.selectedImages,
                timeStamp = currentTime.time,
                fontFamily = notesFragment.selectedFontFamily,
                icEmojiName = emojiName.toString(),
                txtHeadingName = 20,
                txtTextAlign = textAlignment,
                txtColorCode = textColor,
                backgroundValue = notesFragment.backgroundValue,
                emojiRes = getEmoji(emojiName.toString()),
                bgImgRes = emojiName.toString().getEmojiColorForCardBg(),
                cardBgColor = getEmojiColor(emojiName.toString()),
                emojiName = emojiName.toString().getEmojiName(),
                tagsList = notesFragment.listOfAllTags,
                txtHeadingSize = notesFragment.textHeadingAndDescriptionSize?.first ?: 19F,
                desHeadingSize = notesFragment.textHeadingAndDescriptionSize?.second ?: 22F
            )
        }
    }
}

fun List<String>.toEntity(noteId: Int): List<CustomTagEntity> {
    return map {
        CustomTagEntity(tagName = it, noteId = noteId)
    }
}

fun ImageView.setEmoji(emojiName: String, context: Context?) {
    val drawableRes = when (emojiName) {
        "One" -> R.drawable.ic_emoji_one
        "Two" -> R.drawable.ic_emoji_two
        "Three" -> R.drawable.ic_emoji_three
        "Four" -> R.drawable.ic_emoji_four
        "Five" -> R.drawable.ic_emoji_five_new
        "Six" -> R.drawable.ic_emoji_six_new
        else -> R.drawable.ic_emoji_one
    }

    this.setImageDrawable(ContextCompat.getDrawable(context ?: return, drawableRes))
    this.contentDescription = emojiName
}


fun getEmojiColor(emojiName: String): String {
    return when (emojiName) {
        "One" -> "#FF8D95"
        "Two" -> "#FFAC81"
        "Three" -> "#AADAF0"
        "Four" -> "#A29DFB"
        "Five" -> "#FFDE8B"
        "Six" -> "#5EE3A9"
        else -> "#FF8D95"
    }
}

fun getEmoji(emojiName: String): Int {
    return when (emojiName) {
        "One" -> R.drawable.ic_emoji_one
        "Two" -> R.drawable.ic_emoji_two
        "Three" -> R.drawable.ic_emoji_three
        "Four" -> R.drawable.ic_emoji_four
        "Five" -> R.drawable.ic_emoji_five_new
        "Six" -> R.drawable.ic_emoji_six_new
        else -> R.drawable.ic_emoji_one
    }
}

fun String.getEmojiColorForCardBg(): Int {
    return when (this) {
        "One" -> R.drawable.bg_item_one
        "Two" -> R.drawable.bg_item_two
        "Three" -> R.drawable.bg_item_three
        "Four" -> R.drawable.bg_item_four
        "Five" -> R.drawable.bg_item_five
        "Six" -> R.drawable.bg_item_six
        else -> R.drawable.bg_item_one
    }
}

fun String.getEmojiName(): String {
    return when (this) {
        "One" -> "Happy"
        "Two" -> "Angry"
        "Three" -> "Calm"
        "Four" -> "Cheeky"
        "Five" -> "Sad"
        "Six" -> "Meh"
        else -> ""
    }
}


fun TextView.setFont(fontName: String, context: Context?) {
    val typeface = when (fontName) {
        "Intaliana" -> ResourcesCompat.getFont(context ?: return, R.font.italiana_regular)
        "Leckerli" -> ResourcesCompat.getFont(context ?: return, R.font.leckerlione_regular)
        "Margarine" -> ResourcesCompat.getFont(context ?: return, R.font.margarine_regular)
        "Rethink" -> ResourcesCompat.getFont(context ?: return, R.font.rethinksans_regular)
        "Pacifico" -> ResourcesCompat.getFont(context ?: return, R.font.pacifico)
        "Lobster" -> ResourcesCompat.getFont(context ?: return, R.font.lobster_regular)
        else -> null
    }

    typeface?.let {
        this.typeface = it
    }
}

fun ImageView.setBackgroundByIndex(context: Context?, index: Int) {
    val drawableRes = when (index) {
        0 -> R.drawable.color_theme_one
        1 -> R.drawable.background_1
        2 -> R.drawable.background_2
        3 -> R.drawable.background_3
        4 -> R.drawable.background_4
        5 -> R.drawable.background_5
        else -> return
    }
    context?.let {
        this.setImageDrawable(ContextCompat.getDrawable(it, drawableRes))
        Log.e("changeBackground", "onChangeBackground: $index")
    }
}

fun TextView.setTextAlignmentByIndex(index: Int) {
    gravity = when (index) {
        0 -> Gravity.START
        1 -> Gravity.CENTER
        2 -> Gravity.END
        else -> gravity
    }
}

fun TextView.setHeadingSize(textSizeInSp: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp)
}

fun getHeadingSize(index: Int): Float {
    return when (index) {
        0 -> 19f
        1 -> 20f
        2 -> 23f
        else -> 19F
    }
}
