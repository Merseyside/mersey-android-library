package com.merseyside.utils.ext

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings
import android.util.TypedValue
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes

fun Context.getResourceFromAttr(
    attr: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int? {
    theme.resolveAttribute(attr, typedValue, resolveRefs)
    val id = typedValue.resourceId
    return if (id == 0) null
    else id
}

fun Context.getStringFromAttr(
    attr: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): String {
    theme.resolveAttribute(attr, typedValue, resolveRefs)
    return typedValue.string.toString()
}

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Context?.startMapIntent(latitude: Double?, longitude: Double?, dealershipName: String?) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=$latitude,$longitude($dealershipName)")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    this?.packageManager?.let { packageManager ->
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}

fun Context?.startAppSettingsIntent() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", this?.packageName, null)
    intent.data = uri
    this?.packageManager?.let { packageManager ->
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}

fun Context?.startAppDetailsOnGooglePlay() {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("market://details?id=${this?.packageName}")
    this?.packageManager?.let { packageManager ->
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}

fun Context.getDimension(@DimenRes res: Int): Float {
    return com.merseyside.utils.getDimension(this, res)
}

fun Context.getWindowWidth(): Int {
    val metrics = resources.displayMetrics
    return metrics.widthPixels
}

fun Context.getWindowHeight(): Int {
    val metrics = resources.displayMetrics
    return metrics.heightPixels
}

fun Context.getNavigationBarHeight(): Int {
    val result = 0
    val hasMenuKey =
        ViewConfiguration.get(this).hasPermanentMenuKey()
    val hasBackKey =
        KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
    if (!hasMenuKey && !hasBackKey) { //The device has a navigation bar
        val orientation = resources.configuration.orientation
        val resourceId: Int = if (isTablet()) {
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
            return resources.getDimensionPixelSize(resourceId)
        }
    }
    return result
}

fun Context.isTablet(): Boolean {
    return ((resources.configuration.screenLayout
            and Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE)
}

fun Context.getApplicationName(): String {
    val applicationInfo: ApplicationInfo = applicationInfo
    val stringId: Int = applicationInfo.labelRes
    return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else getString(stringId)
}

fun Context.getVersion(): String? {
    return try {
        val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }
}