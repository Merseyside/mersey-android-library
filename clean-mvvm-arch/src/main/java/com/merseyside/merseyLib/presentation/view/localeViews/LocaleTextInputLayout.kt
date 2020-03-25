package com.merseyside.merseyLib.presentation.view.localeViews

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.utils.Logger
import com.merseyside.merseyLib.utils.ext.log

class LocaleTextInputLayout(
    context: Context,
    attributeSet: AttributeSet
) : TextInputLayout(context, attributeSet), ILocaleTextInputLayout {

    @StringRes
    override var hintId: Int? = null
    override var hintArgs: Array<String> = emptyArray()

    init {
        loadAttrs(attributeSet)
    }

    private fun loadAttrs(attributeSet: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.LocaleTextInputLayout, 0, 0)

        hintId = array.getResourceId(R.styleable.LocaleTextInputLayout_android_hint, 0).log()
    }

    override fun getView(): TextInputLayout {
        return this
    }

    override fun getLocaleContext(): Context {
        return context
    }
}