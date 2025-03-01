package com.example.neweasydairy.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.easydiaryandjournalwithlock.databinding.FeelingDialogBinding
import com.example.neweasydairy.interfaces.OnEmojiChangeListener
import com.example.neweasydairy.interfaces.OnTextColorSelectionListener

class FeelingDialog(
    activity: Activity
) : Dialog(activity) {
    private val inflater = activity.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE
    ) as LayoutInflater
    private val binding = FeelingDialogBinding.inflate(inflater)
    private var onEmojiChangeListener:OnEmojiChangeListener?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        binding.apply {
            icEmojiOne.setOnClickListener {
                onEmojiChangeListener?.onEmojiSelected("One")
                dismiss()
            }

            icEmojiTwo.setOnClickListener {
                onEmojiChangeListener?.onEmojiSelected("Two")
                dismiss()
            }
            icEmojiThree.setOnClickListener {
                onEmojiChangeListener?.onEmojiSelected("Three")
                dismiss()
            }
            icEmojiFour.setOnClickListener {
                onEmojiChangeListener?.onEmojiSelected("Four")
                dismiss()
            }
            icEmojiFive.setOnClickListener {
                onEmojiChangeListener?.onEmojiSelected("Five")
                dismiss()
            }
            icEmojiSix.setOnClickListener {
                onEmojiChangeListener?.onEmojiSelected("Six")
                dismiss()
            }
        }
    }

    fun setOnEmojiSelectionListener(listener: OnEmojiChangeListener) {
        onEmojiChangeListener = listener
    }
}