package com.example.neweasydairy.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import com.example.easydiaryandjournalwithlock.databinding.EditTagDialogBinding
import com.example.neweasydairy.data.CustomTagEntity

class EditTagDialog(
    activity: Activity,
    private val label1: String,
    private val label2: String,
    private val label3: String,
    var onUpdateTag: ((CustomTagEntity) -> Unit)? = null,
    var onCancelTag: (() -> Unit)? = null,
) : Dialog(activity) {
    private val inflater = activity.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE
    ) as LayoutInflater
    private val binding = EditTagDialogBinding.inflate(inflater)

    private var currentTag: CustomTagEntity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        setCancelable(true)
        setCanceledOnTouchOutside(true)

        binding.apply {
            txtUploadAudio.text = label1
            editTags.hint = label2
            txtEdit.text = label3

            txtCancel.setOnClickListener {
                onCancelTag?.invoke()
                dismiss()
            }

            txtEdit.setOnClickListener {
                val updatedText = editTags.text.toString().trim()
                if (label3 == "Add") {
                    Log.e("updatedText", "onCreate: $updatedText")
                    onUpdateTag?.invoke(CustomTagEntity(id = 0, tagName = updatedText))
                    return@setOnClickListener
                }
                if (updatedText.isNotEmpty() && currentTag != null) {
                    val updatedTag = currentTag!!.copy(tagName = updatedText)
                    onUpdateTag?.invoke(updatedTag)
                    dismiss()
                } else {
                    Toast.makeText(context, "Please enter valid text", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setTagData(tag: CustomTagEntity) {
        currentTag = tag
        binding.editTags.setText(tag.tagName)
        binding.editTags.setSelection(tag.tagName.length)
    }
}
