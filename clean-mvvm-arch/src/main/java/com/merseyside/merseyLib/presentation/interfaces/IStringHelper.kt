package com.merseyside.merseyLib.presentation.interfaces

import android.content.Context
import androidx.annotation.StringRes
import com.merseyside.merseyLib.presentation.ext.getActualString

interface IStringHelper {

    fun getString(@StringRes id: Int, vararg args: String): String {
        return getString(getLocaleContext(), id, *args)!!
    }

    fun getString(@StringRes id: Int?, vararg args: String): String? {
        return getString(getLocaleContext(), id, *args)
    }

    fun getString(context: Context, @StringRes id: Int?, vararg args: String): String? {
        return context.getActualString(id, *args)
    }

    fun getLocaleContext(): Context
}