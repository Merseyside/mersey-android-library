package com.merseyside.merseyLib.presentation.view.progressBar

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.merseyside.merseyLib.R

class TextProgressBar(context: Context, attributeSet: AttributeSet)
    : LinearLayout(context, attributeSet) {

    private lateinit var progressBar: ProgressBar
    private lateinit var text: TextView
    private lateinit var background: View

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

        textValue = array.getString(R.styleable.ProgressBarAttr_text) ?: ""
        bgColor = array.getColor(R.styleable.ProgressBarAttr_backgroundColor, ContextCompat.getColor(context, R.color.default_progress_bg_color))
        textColor = array.getColor(R.styleable.ProgressBarAttr_android_textColor, ContextCompat.getColor(context, R.color.default_progress_text_color))
        progressColor = array.getColor(R.styleable.ProgressBarAttr_progressColor, ContextCompat.getColor(context, R.color.default_progress_color))
    }

    private fun doLayout() {
        LayoutInflater.from(context).inflate(R.layout.view_progress_bar, this)

        progressBar = findViewById(R.id.text_progress)
        text = findViewById(R.id.text)
        background = findViewById(R.id.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.indeterminateTintList = ColorStateList.valueOf(progressColor!!)
        }

        background.setBackgroundColor(bgColor!!)
        text.setTextColor(textColor!!)

        setText(textValue)
    }

    fun setText(value: String?) {
        textValue = value

        text.text = value

        if (value.isNullOrEmpty()) {
            text.visibility = View.GONE
        } else {
            text.visibility = View.VISIBLE
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        text.visibility = visibility
        progressBar.visibility = visibility
    }


}