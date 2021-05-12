package com.merseyside.archy.presentation.view.progressBar

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.merseyside.archy.R
import com.merseyside.archy.databinding.ViewProgressBarBinding
import com.merseyside.utils.delegate.getValue
import com.merseyside.utils.delegate.viewBinding

class TextProgressBar(context: Context, attributeSet: AttributeSet)
    : LinearLayout(context, attributeSet) {

    private val binding: ViewProgressBarBinding by viewBinding(R.layout.view_progress_bar)

    private var textValue: String? = null

    @ColorInt
    private var bgColor: Int? = null

    @ColorInt
    private var textColor: Int? = null

    @ColorInt
    private var progressColor: Int? = null

    init {
        loadAttrs(attributeSet)
        doLayout()
    }

    private fun loadAttrs(attributeSet: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(attributeSet, R.styleable.ProgressBarAttr, 0, 0)

        //TODO implement attribute delegate
        textValue = array.getString(R.styleable.ProgressBarAttr_text) ?: ""
        bgColor = array.getColor(R.styleable.ProgressBarAttr_backgroundColor, ContextCompat.getColor(context, R.color.default_progress_bg_color))
        textColor = array.getColor(R.styleable.ProgressBarAttr_android_textColor, ContextCompat.getColor(context, R.color.default_progress_text_color))
        progressColor = array.getColor(R.styleable.ProgressBarAttr_progressColor, ContextCompat.getColor(context, R.color.default_progress_color))

        array.recycle()
    }

    private fun doLayout() {

        binding.textProgress.indeterminateTintList = ColorStateList.valueOf(progressColor!!)

        setBackgroundColor(bgColor!!)
        binding.text.setTextColor(textColor!!)

        setText(textValue)
    }

    fun setText(value: String?) {
        textValue = value

        binding.text.text = value

        if (value.isNullOrEmpty()) {
            binding.text.visibility = GONE
        } else {
            binding.text.visibility = VISIBLE
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        binding.text.visibility = visibility
        binding.textProgress.visibility = visibility
    }
}