package com.merseyside.utils

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.utils.preferences.PreferenceManager

object ThemeManager {

    private var savedTheme: Theme? = null

    enum class Theme(internal val themeId: Int) {
        LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
        DARK(AppCompatDelegate.MODE_NIGHT_YES),
        DEFAULT(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        companion object {
            fun getThemeById(themeId: Int): Theme? {
                return values().find { it.themeId == themeId }
            }
        }
    }

    private var preferenceManager: PreferenceManager? = null

    fun setPrefsManager(preferenceManager: PreferenceManager) {
        this.preferenceManager = preferenceManager
    }

    fun setTheme(theme: Theme?) {
        savedTheme = theme
    }

    fun isDarkTheme(configuration: Configuration): Boolean {
        val currentNightMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun isLightTheme(configuration: Configuration) = !isDarkTheme(configuration)

    fun apply(theme: Theme? = savedTheme) {
        if (theme != null) {
            AppCompatDelegate.setDefaultNightMode(theme.themeId)

            preferenceManager?.put(THEME_KEY, theme.themeId)
        } else {
            Logger.log(this, "Theme hasn't been set")
        }
    }

    fun initTheme() {
        preferenceManager?.also {
            apply(getSavedTheme())
        } ?: throw IllegalStateException("Preference manager hasn't been set")
    }

    fun getSavedTheme(): Theme {
        return preferenceManager?.let {
            Theme.getThemeById(it.getInt(THEME_KEY, Theme.DEFAULT.themeId))
        } ?: throw IllegalStateException("Preference manager hasn't been set")
    }

    private const val THEME_KEY = "theme"
}