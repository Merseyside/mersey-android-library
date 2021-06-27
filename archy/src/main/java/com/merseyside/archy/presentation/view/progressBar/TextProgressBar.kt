package com.merseyside.archy.presentation.view.progressBar

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.LinearLayout
import com.merseyside.archy.R
import com.merseyside.archy.databinding.ViewProgressBarBinding
import com.merseyside.utils.delegate.*

class TextProgressBar(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private val binding: ViewProgressBarBinding by viewBinding(R.layout.view_progress_bar)
    private val attrs = AttributeHelper(context, attributeSet)

    private var textValue: String? by attrs.stringOrNull(resName = "text")

    private var bgColor: Int by attrs.color(
        R.color.default_progress_bg_color,
        resName = "backgroundColor"
    )
    private var textColor: Int by attrs.color(
        R.color.default_progress_text_color,
        namespace = Namespace.ANDROID
    )
    private var progressColor: Int by attrs.color(R.color.default_progress_color)

    init {
        doLayout()
    }

    private fun doLayout() {
        orientation = VERTICAL

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