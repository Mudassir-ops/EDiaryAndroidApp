package com.example.neweasydairy.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.TextDialogBinding
import com.example.neweasydairy.interfaces.OnFontSelectionListener
import com.example.neweasydairy.interfaces.OnTextAlignListener
import com.example.neweasydairy.interfaces.OnTextColorSelectionListener
import com.example.neweasydairy.interfaces.OnTextHeadingListener

class TextDialog(activity: Activity) : Dialog(activity) {
    private val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val binding = TextDialogBinding.inflate(inflater)
    private var textAlignListener: OnTextAlignListener? = null
    private var textHeadingListener: OnTextHeadingListener? = null
    private var fontSelectionListener: OnFontSelectionListener? = null
    private var textColorSelectionListener: OnTextColorSelectionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        binding.apply {
            icStartLine.setOnClickListener {
                updateTextAlignTintColors(selectedView = icStartLine)
                textAlignListener?.onTextAlignChanged(0)
            }
            icCenterLine.setOnClickListener {
                updateTextAlignTintColors(selectedView = icCenterLine)
                textAlignListener?.onTextAlignChanged(1)
            }
            icEndLine.setOnClickListener {
                updateTextAlignTintColors(selectedView = icEndLine)
                textAlignListener?.onTextAlignChanged(2)
            }
            icHeadingOne.setOnClickListener {
                updateHeadingTintColors(selectedView = icHeadingOne)
                textHeadingListener?.onTextHeadingChanged(0)
            }
            icHeadingTwo.setOnClickListener {
                updateHeadingTintColors(selectedView = icHeadingTwo)
                textHeadingListener?.onTextHeadingChanged(1)
            }
            icHeadingThree.setOnClickListener {
                updateHeadingTintColors(selectedView = icHeadingThree)
                textHeadingListener?.onTextHeadingChanged(2)
            }

            icBlueColor.setOnClickListener {
                textColorSelectionListener?.onTextColorSelected(Color.parseColor("#458CF0"))
            }
            icBlackColor.setOnClickListener {
                textColorSelectionListener?.onTextColorSelected(Color.parseColor("#334155"))

            }
            icDarkGrayColor.setOnClickListener {
                textColorSelectionListener?.onTextColorSelected(Color.parseColor("#8478BF"))


            }
            icLightGrayColor.setOnClickListener {
                textColorSelectionListener?.onTextColorSelected(Color.parseColor("#0F172A"))


            }
            icPinkColor.setOnClickListener {
                textColorSelectionListener?.onTextColorSelected(Color.parseColor("#4C0821"))

            }
            icGreenishColor.setOnClickListener {
                textColorSelectionListener?.onTextColorSelected(Color.parseColor("#0F2A45"))


            }
            icPurpleColor.setOnClickListener {
                textColorSelectionListener?.onTextColorSelected(Color.parseColor("#64748B"))

            }

            fontIntaliana.setOnClickListener {
                updateFontBackground(selectedFont = fontIntaliana)
                fontSelectionListener?.onFontSelected("Intaliana")
            }
            fontLeckerli.setOnClickListener {
                updateFontBackground(selectedFont = fontLeckerli)
                fontSelectionListener?.onFontSelected("Leckerli")
            }
            fontMargarine.setOnClickListener {
                updateFontBackground(selectedFont = fontMargarine)
                fontSelectionListener?.onFontSelected("Margarine")

            }
            fontRethink.setOnClickListener {
                updateFontBackground(selectedFont = fontRethink)
                fontSelectionListener?.onFontSelected("Rethink")

            }
            fontPacifico.setOnClickListener {
                updateFontBackground(selectedFont = fontPacifico)
                fontSelectionListener?.onFontSelected("Pacifico")
            }
            fontLobster.setOnClickListener {
                updateFontBackground(selectedFont = fontLobster)
                fontSelectionListener?.onFontSelected("Lobster")
            }

        }
    }

    private fun updateTextAlignTintColors(selectedView: ImageView) {
        binding.apply {
            icStartLine.setColorFilter(Color.GRAY)
            icCenterLine.setColorFilter(Color.GRAY)
            icEndLine.setColorFilter(Color.GRAY)

            selectedView.setColorFilter(Color.BLACK)
        }
    }

    private fun updateHeadingTintColors(selectedView: ImageView) {
        binding.apply {
            icHeadingOne.setColorFilter(Color.GRAY)
            icHeadingTwo.setColorFilter(Color.GRAY)
            icHeadingThree.setColorFilter(Color.GRAY)

            selectedView.setColorFilter(Color.BLACK)
        }
    }

    private fun updateFontBackground(selectedFont: AppCompatTextView) {
        binding.apply {
            fontIntaliana.setBackgroundResource(R.drawable.bg_font_family_unselected)
            fontLeckerli.setBackgroundResource(R.drawable.bg_font_family_unselected)
            fontMargarine.setBackgroundResource(R.drawable.bg_font_family_unselected)
            fontRethink.setBackgroundResource(R.drawable.bg_font_family_unselected)
            fontPacifico.setBackgroundResource(R.drawable.bg_font_family_unselected)
            fontLobster.setBackgroundResource(R.drawable.bg_font_family_unselected)

            selectedFont.setBackgroundResource(R.drawable.bg_font_family_selected)
        }
    }

    fun setOnTextAlignListener(listener: OnTextAlignListener) {
        textAlignListener = listener
    }

    fun setOnTextHeadingListener(headingListener: OnTextHeadingListener) {
        textHeadingListener = headingListener
    }

    fun setFontSelectionListener(listener: OnFontSelectionListener) {
        fontSelectionListener = listener
    }

    fun setOnTextColorSelectionListener(listener: OnTextColorSelectionListener) {
        textColorSelectionListener = listener
    }
}

