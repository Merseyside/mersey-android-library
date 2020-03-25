package com.merseyside.merseyLib.presentation.view.localeViews

import android.content.Context
import android.widget.TextView
import androidx.annotation.StringRes

interface ILocaleTextView : ILocaleView {

    var textId: Int?
    var args: Array<String>

    override fun updateLocale(context: Context) {
        setText(context, textId, *args)
    }

    fun setText(@StringRes id: Int, vararg args: String) {
        setText(getLocaleContext(), id, *args)
    }

    fun setText(@StringRes id: Int?, vararg args: String) {
        setText(getLocaleContext(), id, *args)
    }

    fun setText(context: Context, @StringRes id: Int?, vararg args: String) {
        getString(context, id, *args)?.run {
            getView().text = this
        } ?: getView().setText("")

        textId = id
        this.args = arrayOf(*args)
    }

    fun setText(localeData: LocaleData?) {
        if (localeData != null) {
            setText(localeData.context ?: getLocaleContext(), localeData.id, *localeData.args)
        }
    }

    override fun getView(): TextView
}