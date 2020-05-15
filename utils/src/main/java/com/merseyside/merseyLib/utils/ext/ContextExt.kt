package com.merseyside.merseyLib.utils.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

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