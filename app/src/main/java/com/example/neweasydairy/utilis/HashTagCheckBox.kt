package com.example.neweasydairy.utilis

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.example.easydiaryandjournalwithlock.R

class HashTagCheckBox(context: Context, attrs: AttributeSet?) :
    AppCompatCheckBox(context, attrs) {
    init {
        setupBackground(isChecked)
    }

    private fun setupBackground(isChecked: Boolean) {
        background = if (isChecked) {
            ContextCompat.getDrawable(context, R.drawable.ic_hash_checked)
        } else {
            ContextCompat.getDrawable(context, R.drawable.ic_hash)
        }
    }

    override fun setChecked(isChecked: Boolean) {
        super.setChecked(isChecked)
        setupBackground(isChecked)
    }
}
