package com.example.neweasydairy.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.easydiaryandjournalwithlock.databinding.BackgroundDialogBinding
import com.example.neweasydairy.fragments.noteFragment.CreateNoteViewModel
import com.example.neweasydairy.interfaces.OnChangeBackgroundListener

class ChangeBackground(
    activity: Activity, private val createNoteViewModel: CreateNoteViewModel
) : Dialog(activity) {
    private val inflater =
        activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val binding = BackgroundDialogBinding.inflate(inflater)
    private var changeBackgroundListener: OnChangeBackgroundListener? = null


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
            imgFromGallery.setOnClickListener {
                dismiss()
                changeBackgroundListener?.onChangeBackground(0)
                createNoteViewModel.setBackgroundState(0)
            }
            imgBackground2.setOnClickListener {
                changeBackgroundListener?.onChangeBackground(1)
                createNoteViewModel.setBackgroundState(1)
                dismiss()
            }
            imgBackground3.setOnClickListener {
                changeBackgroundListener?.onChangeBackground(2)
                createNoteViewModel.setBackgroundState(2)
                dismiss()
            }
            imgBackground4.setOnClickListener {
                changeBackgroundListener?.onChangeBackground(3)
                createNoteViewModel.setBackgroundState(3)
                dismiss()
            }
            imgBackground5.setOnClickListener {
                changeBackgroundListener?.onChangeBackground(4)
                createNoteViewModel.setBackgroundState(4)
                dismiss()
            }
            imgBackground6.setOnClickListener {
                changeBackgroundListener?.onChangeBackground(5)
                createNoteViewModel.setBackgroundState(5)
                dismiss()
            }
        }
    }

    fun setonChangeBackground(listener: OnChangeBackgroundListener) {
        changeBackgroundListener = listener
    }
}