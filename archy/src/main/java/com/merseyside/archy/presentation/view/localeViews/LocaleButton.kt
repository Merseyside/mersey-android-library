package com.merseyside.archy.presentation.view.localeViews

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import com.merseyside.archy.R
import com.merseyside.archy.presentation.activity.BaseActivity
import com.merseyside.utils.view.ext.getActivity

class LocaleButton(
    context: Context,
    attributeSet: AttributeSet,
    defStyle: Int
) : AppCompatButton(context, attributeSet, defStyle), ILocaleTextView {

    constructor(context: Context,
                attributeSet: AttributeSet): this(context, attributeSet, androidx.appcompat.R.attr.buttonStyle)

    @StringRes
    override var textId: Int? = null
    override var args: Array<String> = emptyArray()

    init {
        loadAttrs(attributeSet)
        updateLocale()
    }

    private fun loadAttrs(attributeSet: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(attributeSet, R.styleable.LocaleButton, 0, 0)

        textId = array.getResourceId(R.styleable.LocaleButton_android_text, 0)
        array.recycle()
    }

    override fun getView(): TextView {
        return this
    }

    override fun getLocaleContext(): Context {
        return (getActivity() as BaseActivity).getContext()
    }
}