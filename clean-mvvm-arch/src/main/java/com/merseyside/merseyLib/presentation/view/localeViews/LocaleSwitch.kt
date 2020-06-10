package com.merseyside.merseyLib.presentation.view.localeViews

import android.content.Context
import android.util.AttributeSet
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.StringRes
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.presentation.activity.BaseActivity
import com.merseyside.merseyLib.utils.ext.getActivity

class LocaleSwitch(
    context: Context,
    attributeSet: AttributeSet
) : Switch(context, attributeSet), ILocaleTextView {

    @StringRes
    override var textId: Int? = null
    override var args: Array<String> = emptyArray()

    init {
        loadAttrs(attributeSet)
        updateLocale()
    }

    private fun loadAttrs(attributeSet: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(attributeSet, R.styleable.LocaleSwitch, 0, 0)

        textId = array.getResourceId(R.styleable.LocaleSwitch_android_text, 0)
        array.recycle()
    }

    override fun getView(): TextView {
        return this
    }

    override fun getLocaleContext(): Context {
        return (getActivity() as BaseActivity).getContext()
    }
}