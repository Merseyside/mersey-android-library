package com.merseyside.archy.presentation.view.localeViews

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import com.merseyside.archy.R
import com.merseyside.archy.presentation.activity.BaseActivity
import com.merseyside.utils.view.ext.getActivity

class LocaleEditText(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatEditText(context, attributeSet), ILocaleEditText {

    @StringRes
    override var textId: Int? = null
    override var args: Array<String> = emptyArray()

    @StringRes
    override var hintId: Int? = null
    override var hintArgs: Array<String> = emptyArray()

    init {
        loadAttrs(attributeSet)
        updateLocale()
    }

    private fun loadAttrs(attributeSet: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.LocaleEditText, 0, 0)

        textId = array.getResourceId(R.styleable.LocaleEditText_android_text, 0)
        hintId = array.getResourceId(R.styleable.LocaleEditText_android_hint, 0)
        array.recycle()
    }

    override fun getView(): EditText {
        return this
    }

    override fun getLocaleContext(): Context {
        return (getActivity() as BaseActivity).getContext()
    }
}