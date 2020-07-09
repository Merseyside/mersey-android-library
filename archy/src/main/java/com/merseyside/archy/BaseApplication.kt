package com.merseyside.archy

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import com.merseyside.utils.LocaleManager
import com.merseyside.utils.getLocalizedContext
import java.lang.Exception
import java.util.*

abstract class BaseApplication : Application() {

    private lateinit var localeManager: LocaleManager

    lateinit var context: Context
        private set

    override fun attachBaseContext(base: Context) {
        localeManager = LocaleManager(base)
        context = getLocalizedContext(localeManager)

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

    companion object {
        private const val TAG = "BaseApplication"
    }
}