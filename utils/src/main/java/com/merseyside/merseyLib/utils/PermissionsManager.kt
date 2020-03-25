package com.merseyside.merseyLib.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlin.math.pow

/**
 * Created by ivan_ on 10.12.2017.
 */
object PermissionsManager {
    fun verifyStoragePermissions(
        activity: Activity?,
        PERMISSIONS: Array<String>,
        code: Int
    ) {
        if (isRequestCodeValid(code)) {
            if (!isPermissionsGranted(activity, PERMISSIONS)) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    PERMISSIONS,
                    code
                )
            }
        } else throw IllegalArgumentException("Request code is not valid")
    }

    fun verifyStoragePermissions(
        fragment: Fragment,
        PERMISSIONS: Array<String>,
        code: Int
    ) {
        if (isRequestCodeValid(code)) {
            if (!isPermissionsGranted(fragment.context, PERMISSIONS)) {
                fragment.requestPermissions(
                    PERMISSIONS,
                    code
                )
            }
        } else throw IllegalArgumentException("Request code is not valid")
    }

    fun isPermissionsGranted(
        context: Context?,
        PERMISSIONS_STORAGE: Array<String>
    ): Boolean {
        for (permission in PERMISSIONS_STORAGE) {
            val granted = ActivityCompat.checkSelfPermission(context!!, permission)
            if (granted != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    private fun isRequestCodeValid(code: Int): Boolean {
        return code <= (2.0.pow(16.0))-1
    }
}