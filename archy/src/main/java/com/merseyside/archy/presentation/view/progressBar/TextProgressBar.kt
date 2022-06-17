package com.merseyside.archy.presentation.view.progressBar

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.merseyside.archy.R
import com.merseyside.archy.databinding.ViewProgressBarBinding
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.delegate.color
import com.merseyside.utils.delegate.getValue
import com.merseyside.utils.delegate.stringOrNull
import com.merseyside.utils.delegate.viewBinding
import com.merseyside.utils.getClassName
import com.merseyside.utils.view.ext.getResourceFromAttr

class TextProgressBar(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) :
    LinearLayout(context, attributeSet) {

    constructor(context: Context, attributeSet: AttributeSet): this(
        context,
        attributeSet,
        R.attr.textProgressBarStyle
    )

    private val binding: ViewProgressBarBinding by viewBinding(R.layout.view_progress_bar)
    private val attrs = AttributeHelper(
        context,
        attributeSet,
        R.styleable.TextProgressBar,
        getClassName(),
        defStyleAttr,
        styleableNamePrefix = "progress"
    )

    private var textValue: String? by attrs.stringOrNull(resName = "text")
    private var bgColor: Int by attrs.color(
        getResourceFromAttr(R.attr.colorSurface) ?: 0,
        resName = "backgroundColor"
    )

    private var textColor: Int by attrs.color(R.color.default_progress_text_color)
    private var progressColor: Int by attrs.color(R.color.default_progress_color)

    init {
        doLayout()
    }

    private fun doLayout() {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        isClickable = true
        isFocusable = true

        binding.textProgress.indeterminateTintList = ColorStateList.valueOf(progressColor)

        setBackgroundColor(bgColor)
        binding.text.setTextColor(textColor)

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