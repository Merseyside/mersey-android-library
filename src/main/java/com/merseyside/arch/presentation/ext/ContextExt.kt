package com.merseyside.archy.ext

import android.content.Context
import androidx.annotation.StringRes
import com.merseyside.archy.BaseApplication
import com.merseyside.merseyLib.kotlin.Logger


fun Context.getActualString(@StringRes id: Int?, vararg args: String?): String? {
    return if (id != null) {
        val formattedArgs = args.map {
            it ?: ""
        }.toTypedArray()

        if (this.applicationContext is BaseApplication) {
            return try {
                (this.applicationContext as BaseApplication).getActualString(id, *formattedArgs)
            } catch (e: Exception) {
                Logger.logErr("ContextExt", "Resource not found! [$id]")
                return null
            }
        } else {
            this.getString(id, *args)
        }
    } else {
        null
    }
}

fun getString(context: Context, @StringRes id: Int, vararg args: String): String {
    return context.getActualString(id, *args)!!
}

fun getString(context: Context, @StringRes id: Int?, vararg args: String?): String? {
    return context.getActualString(id, *args)
}

private const val TAG = "ContextExt"