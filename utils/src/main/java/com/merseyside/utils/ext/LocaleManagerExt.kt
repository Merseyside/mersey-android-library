package com.merseyside.utils.ext

import android.content.Context
import com.merseyside.utils.LocaleManager

fun LocaleManager.getLocalizedContext(): Context {
    return if (language.isNotEmpty()) {
        setLocale()
    } else {
        context
    }
}