package com.merseyside.archy.presentation.view.localeViews

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import com.merseyside.archy.R
import com.merseyside.archy.presentation.activity.BaseActivity
import com.merseyside.utils.view.ext.getActivity
import com.merseyside.merseyLib.kotlin.extensions.log

class LocaleTextInputLayout(
    context: Context,
    attributeSet: AttributeSet
) : TextInputLayout(context, attributeSet), ILocaleTextInputLayout {

    @StringRes
    override var hintId: Int? = null
    override var hintArgs: Array<String> = emptyArray()

    init {
        loadAttrs(attributeSet)
        updateLocale()
    }

    private fun loadAttrs(attributeSet: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.LocaleTextInputLayout, 0, 0)

        hintId = array.getResourceId(R.styleable.LocaleTextInputLayout_android_hint, 0).log()
        array.recycle()
    }

    override fun getView(): TextInputLayout {
        return this
    }

    override fun getLocaleContext(): Context {
        return (getActivity() as BaseActivity).getContext()
    }
}