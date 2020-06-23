package com.merseyside.merseyLib.utils.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.merseyside.merseyLib.utils.time.Millis
import com.merseyside.merseyLib.utils.time.TimeUnit

class PreferenceManager private constructor(
    context: Context,
    preference_filename: String
) {

    class Builder(private val context: Context) {
        private var filename: String? = null

        fun setFilename(filename: String): Builder {
            this.filename = filename
            return this
        }

        @Throws(IllegalArgumentException::class)
        fun build(): PreferenceManager {
            return PreferenceManager(
                context,
                filename ?: "${context.packageName}.preferences"
            )
        }
    }

    private var prefs: SharedPreferences =
        context.getSharedPreferences(preference_filename, Context.MODE_PRIVATE)

    operator fun contains(preference: String?): Boolean {
        return prefs.contains(preference)
    }


    fun setOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener?) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

//    fun setOnSharedPreferenceChangeListener(onChange: (key: String) -> Unit): OnSharedPreferenceChangeListener {
//        val listener = OnSharedPreferenceChangeListener { _, key ->
//            if (key != null) {
//                onChange.invoke(key)
//            }
//        }
//
//        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
//        return listener
//    }

    fun removeOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener?) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun put(preference: String?, value: Int) {
        prefs.edit().apply {
            putInt(preference, value)
            apply()
        }
    }

    fun put(preference: String?, value: String?) {
        prefs.edit().apply {
            putString(preference, value)
            apply()
        }
    }

    fun put(preference: String?, value: Boolean) {
        prefs.edit().apply {
            putBoolean(preference, value)
            apply()
        }
    }

    fun put(preference: String?, value: Float) {
        prefs.edit().apply {
            putFloat(preference, value)
            apply()
        }
    }

    fun put(preference: String?, value: Long) {
        prefs.edit().apply {
            putLong(preference, value)
            apply()
        }
    }

    fun put(preference: String?, value: TimeUnit) {
        prefs.edit().apply {
            putLong(preference, value.toMillisLong())
            apply()
        }
    }

    fun getString(
        preference: String,
        default_value: String
    ): String {
        return prefs.getString(preference, default_value)!!
    }

    fun getNullableString(
        preference: String,
        defaultValue: String? = null
    ): String? {
        return prefs.getString(preference, defaultValue)
    }

    fun getBool(
        preference: String,
        default_value: Boolean
    ): Boolean {
        return prefs.getBoolean(preference, default_value)
    }

    fun getInt(preference: String, defaultValue: Int): Int {
        return prefs.getInt(preference, defaultValue)
    }

    fun getLong(preference: String, defaultValue: Long): Long {
        return prefs.getLong(preference, defaultValue)
    }

    fun getFloat(preference: String, defaultValue: Float): Float {
        return prefs.getFloat(preference, defaultValue)
    }

    fun getTimeUnit(preference: String, defaultValue: TimeUnit): TimeUnit {
        return getTimeUnit(preference, defaultValue.toMillisLong())
    }

    fun getTimeUnit(preference: String, defaultValue: Long): TimeUnit {
        return Millis(prefs.getLong(preference, defaultValue))
    }
}