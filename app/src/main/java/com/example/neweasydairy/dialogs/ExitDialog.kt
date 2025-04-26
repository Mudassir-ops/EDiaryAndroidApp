package com.example.neweasydairy.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.easydiaryandjournalwithlock.databinding.ExitDialogBinding
import com.example.neweasydairy.utilis.feedBackWithEmail
import kotlin.system.exitProcess

class ExitDialog(
    activity: Activity
) : Dialog(activity) {
    private val inflater = activity.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE
    ) as LayoutInflater
    private val binding = ExitDialogBinding.inflate(inflater)

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
            txtCancel.setOnClickListener { dismiss() }
            txtExit.setOnClickListener { exitProcess(0) }
            binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                binding.ratingBar.rating = 0F
                when (rating.toInt()) {
                    in 1..3 -> {
                       context.feedBackWithEmail(title = "Feedback", message = "Any Feedback", emailId = "Cisco7865@gmail.com")
                        dismiss()
                    }
                    4, 5 -> {
                        val packageName = context.packageName
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                        context.startActivity(intent)
                        dismiss()
                    }
                }
            }
        }
    }
}