package com.merseyside.merseyLib.presentation.view.localeViews

import android.content.Context
import android.view.View
import com.merseyside.merseyLib.presentation.interfaces.IStringHelper

interface ILocaleView : IStringHelper {

    fun updateLocale(context: Context = getLocaleContext())

    fun getView(): View
}