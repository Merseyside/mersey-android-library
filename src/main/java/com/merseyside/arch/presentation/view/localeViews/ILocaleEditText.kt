package com.merseyside.archy.presentation.view.localeViews

import android.content.Context
import android.widget.EditText
import androidx.annotation.StringRes

interface ILocaleEditText : ILocaleTextView {

    var hintId: Int?
    var hintArgs: Array<String>

    override fun updateLocale(context: Context) {
        super.updateLocale(context)

        setHint(context, hintId)
    }

    fun setHint(@StringRes id: Int, vararg args: String) {
        setHint(getLocaleContext(), id, *args)
    }

    fun setHint(@StringRes id: Int?, vararg args: String) {
        setHint(getLocaleContext(), id, *args)
    }

    fun setHint(localeData: LocaleData?) {
        if (localeData != null) {
            setHint(localeData.context ?: getLocaleContext(), localeData.id, *localeData.args)
        }
    }

    fun setHint(context: Context, @StringRes id: Int?, vararg args: String) {

        getString(context, id, *args)?.run {
            getView().hint = this
        } ?: getView().setHint("")

        hintId = id
        this.hintArgs = arrayOf(*args)
    }

    override fun getView(): EditText
}