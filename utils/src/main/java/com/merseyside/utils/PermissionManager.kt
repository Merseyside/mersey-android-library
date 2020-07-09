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
object PermissionManager {
    fun requestPermissions(
        activity: Activity?,
        vararg permissions: String,
        requestCode: Int
    ) {
        if (isRequestCodeValid(requestCode)) {
            if (!isPermissionsGranted(activity, *permissions)) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    permissions,
                    requestCode
                )
            }
        } else throw IllegalArgumentException("Request code is not valid")
    }

    fun requestPermissions(
        fragment: Fragment,
        vararg permissions: String,
        requestCode: Int
    ) {
        if (isRequestCodeValid(requestCode)) {
            if (!isPermissionsGranted(fragment.context, *permissions)) {
                fragment.requestPermissions(
                    permissions,
                    requestCode
                )
            }
        } else throw IllegalArgumentException("Request code is not valid")
    }

    fun isPermissionsGranted(
        context: Context?,
        vararg permissions: String
    ): Boolean {
        for (permission in permissions) {
            val granted = checkSelfPermission(context!!, permission)
            if (granted != PackageManager.PERMISSION_GRANTED) return false
        }

        return true
    }

    private fun isRequestCodeValid(code: Int): Boolean {
        return code <= (2.0.pow(16.0))-1
    }
}