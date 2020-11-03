package com.merseyside.kmpMerseyLib.utils

actual object Logger {
    actual var isEnabled: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
    actual var isDebug: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}

    actual fun log(tag: Any?, msg: Any?) {
    }

    actual fun log(msg: Any?) {
    }

    actual fun logErr(tag: Any?, msg: Any?) {
    }

    actual fun logErr(msg: Any?) {
    }

    actual fun logErr(throwable: Throwable) {
    }

    actual fun logInfo(tag: Any?, msg: Any?) {
    }

    actual fun logInfo(msg: Any?) {
    }

    actual fun logWtf(tag: Any?, msg: Any?) {
    }

    actual fun logWtf(msg: Any?) {
    }

    actual val TAG: String
        get() = TODO("Not yet implemented")
}