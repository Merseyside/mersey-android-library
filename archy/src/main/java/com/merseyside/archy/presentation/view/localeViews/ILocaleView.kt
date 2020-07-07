package com.merseyside.archy.presentation.view.localeViews

import android.content.Context
import android.view.View
import com.merseyside.archy.presentation.interfaces.IStringHelper

interface ILocaleView : IStringHelper {

    fun updateLocale(context: Context = getLocaleContext())

    fun getView(): View
}