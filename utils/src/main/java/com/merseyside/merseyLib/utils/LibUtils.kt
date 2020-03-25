@file:JvmName("LibUtils")
package com.merseyside.merseyLib.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import com.merseyside.merseyLib.utils.time.TimeUnit
import java.util.*

fun getLocalizedContext(localeManager: LocaleManager): Context {
    return if (localeManager.language.isNotEmpty()) {
        localeManager.setLocale()
    } else {
        localeManager.context
    }
}

fun randomBool(positiveProbability: Float): Boolean {
    return when {
        positiveProbability >= 1f -> true
        positiveProbability <= 0f -> false

        else -> {
            val rand = Random()
            rand.nextFloat() <= positiveProbability
        }
    }
}

fun convertPixelsToDp(context: Context, px: Int): Float {
    val density = context.resources.displayMetrics.density
    return px / density

}

fun convertDpToPixel(context: Context, dp: Float): Float {
    val density = context.resources.displayMetrics.density
    return dp * density
}

fun generateRandomString(length: Int): String {
    if (length > 0) {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..length)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    return ""
}

fun getWindowWidth(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return metrics.widthPixels
}

fun getNavigationBarHeight(context: Context): Int {
    val result = 0
    val hasMenuKey =
        ViewConfiguration.get(context).hasPermanentMenuKey()
    val hasBackKey =
        KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
    if (!hasMenuKey && !hasBackKey) { //The device has a navigation bar
        val resources = context.resources
        val orientation = context.resources.configuration.orientation
        val resourceId: Int
        resourceId = if (isTablet(context)) {
            resources.getIdentifier(
                if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_height_landscape",
                "dimen",
                "android"
            )
        } else {
            resources.getIdentifier(
                if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_width",
                "dimen",
                "android"
            )
        }
        if (resourceId > 0) {
            return context.resources.getDimensionPixelSize(resourceId)
        }
    }
    return result
}

fun isTablet(context: Context): Boolean {
    return ((context.resources.configuration.screenLayout
            and Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE)
}

fun getApplicationName(context: Context): String? {
    val applicationInfo: ApplicationInfo = context.applicationInfo
    val stringId: Int = applicationInfo.labelRes
    return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
        stringId
    )
}

fun mainThread(onMain: () -> Unit): Handler {
    val handler = Handler(Looper.getMainLooper())
    handler.post(onMain)
    return handler
}

fun delayedMainThread(delay: TimeUnit, onMain: () -> Unit): Handler {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed(onMain, delay.toMillisLong())
    return handler
}

fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

fun isExternalStorageReadable(): Boolean {
    val state = Environment.getExternalStorageState()
    return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
}