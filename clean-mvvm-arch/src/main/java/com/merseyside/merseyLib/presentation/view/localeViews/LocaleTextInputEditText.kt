package com.merseyside.merseyLib.presentation.view.localeViews

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputEditText
import com.merseyside.merseyLib.R

class LocaleTextInputEditText(
    context: Context,
    attributeSet: AttributeSet
): TextInputEditText(context, attributeSet), ILocaleEditText {

    @StringRes
    override var textId: Int? = null
    override var args: Array<String> = emptyArray()

    @StringRes
    override var hintId: Int? = null
    override var hintArgs: Array<String> = emptyArray()

    init {
        loadAttrs(attributeSet)
    }

    private fun loadAttrs(attributeSet: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.LocaleEditText, 0, 0)

        textId = array.getResourceId(R.styleable.LocaleEditText_android_text, 0)
        hintId = array.getResourceId(R.styleable.LocaleEditText_android_hint, 0)
    }

    override fun getView(): EditText {
        return this
    }

    override fun getLocaleContext(): Context {
        return context
    }
}