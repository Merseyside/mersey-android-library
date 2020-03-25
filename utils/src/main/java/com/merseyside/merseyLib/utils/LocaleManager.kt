package com.merseyside.merseyLib.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
import java.util.*

class LocaleManager(var context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "locale_pref", Context.MODE_PRIVATE
    )

    var language: String = prefs.getString(LANGUAGE_KEY, getCurrentLocale().language)!!

    fun setLocale(): Context {
        return updateResources(language)
    }

    fun setNewLocale(language: String): Context {
        persistLanguage(language)
        return updateResources(language)
    }

    @SuppressLint("ApplySharedPref")
    private fun persistLanguage(language: String) {
        this.language = language
        prefs.edit().putString(LANGUAGE_KEY, language).commit()
    }

    private fun updateResources(language: String): Context {

        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)

        this.context = if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR1) {
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
            context
        }

        return this.context
    }

    fun getCurrentLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }

    companion object {
        private const val LANGUAGE_KEY = "language_key"
    }
}