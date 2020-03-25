package com.merseyside.merseyLib.utils

import android.util.Log
import com.merseyside.merseyLib.BuildConfig

object Logger {

    var isEnabled: Boolean = true
    var isDebugOnly = true

    fun log(tag: Any? = null, msg: Any? = "Empty msg") {
        if (isEnabled && (!isDebugOnly || BuildConfig.DEBUG)) {
            Log.d(adoptTag(tag), adoptMsg(msg))
        }
    }

    fun log(msg: Any? = "Empty msg") {
        log("", msg)
    }

    fun logErr(tag: Any? = null, msg: Any? = "Empty error") {
        if (isEnabled && (!isDebugOnly || BuildConfig.DEBUG)) {
            Log.e(adoptTag(tag), adoptMsg(msg))
        }
    }

    fun logErr(msg: Any? = "Empty error") {
        logErr("", msg)
    }

    fun logInfo(tag: Any? = null, msg: Any?) {
        if (isEnabled && (!isDebugOnly || BuildConfig.DEBUG)) {
            Log.i(adoptTag(tag), adoptMsg(msg))
        }
    }

    fun logInfo(msg: Any?) {
        logInfo("", msg)
    }

    fun logWtf(tag: Any? = null, msg: Any? = "wtf?") {
        if (isEnabled && (!isDebugOnly || BuildConfig.DEBUG)) {
            Log.wtf(adoptTag(tag), adoptMsg(msg))
        }
    }

    fun logWtf(msg: Any? = "wtf?") {
        logWtf("", msg)
    }

    fun logErr(throwable: Throwable) {
        if (isEnabled && (!isDebugOnly || BuildConfig.DEBUG)) {
            throwable.printStackTrace()
        }
    }

    private fun adoptTag(tag: Any?): String {

        return if (tag != null) {
            val strTag = if (tag is String) {
                tag
            } else {
                tag.javaClass.simpleName
            }

            if (strTag.isEmpty()) {
                TAG
            } else {
                strTag
            }
        } else {
            TAG
        }
    }

    private fun adoptMsg(msg: Any?): String {
       return when (msg) {
           null -> {
               "null"
           }

           is String -> {
               msg
           }

           is Collection<*> -> {
               if (msg.isEmpty()) {
                   "Empty collection"
               } else {
                   msg.toString()
               }
           }

           else -> {
               msg.toString()
           }
       }
    }

    const val TAG = "Logger"
}