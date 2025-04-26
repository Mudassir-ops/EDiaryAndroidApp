package com.example.neweasydairy.fragments.noteFragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentCreateNotesBinding
import com.example.neweasydairy.utilis.Objects.FROM_HOME_FRAGMENT
import com.example.neweasydairy.utilis.toast
import java.util.Date


fun FragmentCreateNotesBinding?.clickListener(context: Context, fragment: CreateNotesFragment) {
    this?.apply {
        val icons = listOf(icGrid, icText, icImageNote, icHash)

        icHash.setOnClickListener {
            Log.d("selectedImages", "Before navigating: ${fragment.selectedImages}")
            fragment.viewModel.setSelectedImages(fragment.selectedImages)
            fragment.viewModel.title = fragment.binding?.txtTitle?.text.toString()
            fragment.viewModel.description = fragment.binding?.txtEdDescription?.text.toString()
            fragment.viewModel.icEmojiName = fragment.binding?.icEmoji?.contentDescription.toString()
            if (fragment.findNavController().currentDestination?.id == R.id.createNotesFragment) {
                fragment.findNavController().navigate(R.id.action_createNotesFragment_to_tagsFragment)
            }
            resetIconColors(context, icons)
            icHash.setColorFilter(ContextCompat.getColor(context, R.color.app_color))
        }

        icBack.setOnClickListener {
            fragment.findNavController().navigateUp()
        }
        icEmoji.setOnClickListener {
            fragment.feelingDialogBinding?.show()
        }
        icGrid.setOnClickListener {
            resetIconColors(context, icons)
            icGrid.setColorFilter(ContextCompat.getColor(context, R.color.app_color))
            fragment.backgroundDialog?.show()
        }
        icImageNote.setOnClickListener {
            fragment.viewModel.title = fragment.binding?.txtTitle?.text.toString()
            fragment.viewModel.description = fragment.binding?.txtEdDescription?.text.toString()
            fragment.viewModel.icEmojiName = fragment.binding?.icEmoji?.contentDescription.toString()
            resetIconColors(context, icons)
            icImageNote.setColorFilter(ContextCompat.getColor(context, R.color.app_color))
            fragment.photoDialog?.show()
        }
        icText.setOnClickListener {
            resetIconColors(context, icons)
            icText.setColorFilter(ContextCompat.getColor(context, R.color.app_color))
            fragment.textDialog?.show()
        }
        txtSave.setOnClickListener {

                when(fragment.argument){
                    FROM_HOME_FRAGMENT->{
                        Log.d("saqibRehman", "txtSave: FROM_HOME_FRAGMENT")
                    }
                    else->{
                        Log.d("saqibRehman", "txtSave: else")

                        insertData(notesFragment = fragment)
                        fragment.requireActivity().toast("Data Save Successfully")
                        if (fragment.findNavController().currentDestination?.id == R.id.createNotesFragment) {
                            fragment.findNavController().navigate(R.id.action_createNotesFragment_to_mainFragment)
                        }
                    }

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

fun insertData(notesFragment: CreateNotesFragment){
    val description = notesFragment.binding?.txtEdDescription?.text.toString()
    val title = notesFragment.binding?.txtTitle?.text.toString()

    val currentTime = Date()
    val textSize = notesFragment.binding?.txtTitle?.textSize?.toInt() ?: 0
    val textColor = notesFragment.binding?.txtTitle?.currentTextColor ?: 0
    val emojiName = notesFragment.binding?.icEmoji?.contentDescription

    var textAlignment = 0
    when (notesFragment.binding?.txtTitle?.gravity) {
        8388661 -> {
            textAlignment = 2
        }
        17 -> {
            textAlignment = 1
        }
        else -> {
            textAlignment = 0
        }
    }



    notesFragment.binding?.apply {

            notesFragment.viewModel.insertNoteData(
                title = title,
                description = description,
                color = notesFragment.backgroundValue,
                imageFiles = notesFragment.selectedImages,
                timeStamp = currentTime.time,
                fontFamily = notesFragment.selectedFontFamily,
                icEmojiName = emojiName.toString(),
                txtHeadingName = textSize,
                txtTextAlign = textAlignment,
                txtColorCode = textColor,
                backgroundValue = notesFragment.backgroundValue,
                tagsText = notesFragment.viewModel.tagList.toString()
            )

    }
}

fun ImageView.setEmoji(emojiName: String, context: Context?) {
    val drawableRes = when(emojiName) {
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

fun TextView.setFont(fontName: String, context: Context?) {
    val typeface = when (fontName) {
        "Intaliana" -> ResourcesCompat.getFont(context?:return, R.font.italiana_regular)
        "Leckerli" -> ResourcesCompat.getFont(context?:return, R.font.leckerlione_regular)
        "Margarine" -> ResourcesCompat.getFont(context?:return, R.font.margarine_regular)
        "Rethink" -> ResourcesCompat.getFont(context?:return, R.font.rethinksans_regular)
        "Pacifico" -> ResourcesCompat.getFont(context?:return, R.font.pacifico)
        "Lobster" -> ResourcesCompat.getFont(context?:return, R.font.lobster_regular)
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

fun TextView.setHeadingSize(index: Int) {
    val textSizeInSp = when (index) {
        0 -> 17f
        1 -> 20f
        2 -> 23f
        else -> textSize
    }
    setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp)
}
