package com.merseyside.kmpMerseyLib.utils

import android.util.Log

actual object Logger {
    actual var isEnabled: Boolean = true
    actual var isDebug = true

    actual fun log(tag: Any?, msg: Any?) {
        if (isLogging()) {
            Log.d(adoptTag(tag), adoptMsg(msg))
        }
    }

    actual fun log(msg: Any?) {
        log("", msg)
    }

    actual fun logErr(tag: Any?, msg: Any?) {
        if (isLogging()) {
            Log.e(adoptTag(tag), adoptMsg(msg))
        }
    }

    actual fun logErr(msg: Any?) {
        logErr("", msg)
    }

    actual fun logInfo(tag: Any?, msg: Any?) {
        if (isLogging()) {
            Log.i(adoptTag(tag), adoptMsg(msg))
        }
    }

    actual fun logInfo(msg: Any?) {
        logInfo("", msg)
    }

    actual fun logWtf(tag: Any?, msg: Any?) {
        if (isLogging()) {
            Log.wtf(adoptTag(tag), adoptMsg(msg))
        }
    }

    actual fun logWtf(msg: Any?) {
        logWtf("", msg)
    }

    actual fun logErr(throwable: Throwable) {
        if (isLogging()) {
            throwable.printStackTrace()
        }
    }

    private fun isLogging(): Boolean {
        return isEnabled && isDebug
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

    actual val TAG = "Logger"
}