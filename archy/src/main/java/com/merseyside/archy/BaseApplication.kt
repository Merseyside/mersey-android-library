package com.merseyside.archy

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import com.merseyside.utils.LocaleManager
import com.merseyside.utils.ext.getLocalizedContext
import java.lang.Exception
import java.util.*
import kotlin.jvm.Throws

abstract class BaseApplication : Application() {

    private lateinit var localeManager: LocaleManager

    lateinit var context: Context
        private set

    override fun attachBaseContext(base: Context) {
        localeManager = LocaleManager(base)
        context = localeManager.getLocalizedContext()

        super.attachBaseContext(context)
    }

    fun setLanguage(language: String): Context {
        context = localeManager.setNewLocale(language)
        return context
    }

    fun getLanguage(): String {
        return localeManager.language
    }

    @Throws(Exception::class)
    internal fun getActualString(@StringRes id: Int, vararg args: String): String {
        return context.getString(id, *args)
    }

    fun getLocale(): Locale {
        return localeManager.getCurrentLocale()
    }
}