package com.merseyside.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import kotlin.math.pow

/**
 * Created by ivan_ on 10.12.2017.
 */
fun Activity.requestPermissions(
    vararg permissions: String,
    requestCode: Int
) {
    if (isRequestCodeValid(requestCode)) {
        if (!isPermissionsGranted(*permissions)) {
            ActivityCompat.requestPermissions(
                this,
                permissions,
                requestCode
            )
        }
    } else throw IllegalArgumentException("Request code is not valid")
}

fun Fragment.requestPermissions(
    vararg permissions: String,
    requestCode: Int
) {
    if (isRequestCodeValid(requestCode)) {
        if (!requireContext().isPermissionsGranted(*permissions)) {
            requestPermissions(
                permissions,
                requestCode
            )
        }
    } else throw IllegalArgumentException("Request code is not valid")
}

fun Context.isPermissionsGranted(
    vararg permissions: String
): Boolean {
    for (permission in permissions) {
        val granted = checkSelfPermission(this, permission)
        if (granted != PackageManager.PERMISSION_GRANTED) return false
    }

    return true
}

fun Fragment.isPermissionsGranted(vararg permissions: String): Boolean {
    return requireContext().isPermissionsGranted(*permissions)
}

private fun isRequestCodeValid(code: Int): Boolean {
    return code <= (2.0.pow(16.0)) - 1
}