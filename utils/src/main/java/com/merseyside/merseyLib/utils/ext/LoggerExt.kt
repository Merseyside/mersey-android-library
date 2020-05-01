package com.merseyside.merseyLib.utils.ext

import com.merseyside.merseyLib.utils.Logger

fun <T> T.log(tag: Any = Logger.TAG, prefix: Any? = null, suffix: Any? = null): T {
    val msg = "${prefix ?: ""} $this ${suffix ?: ""}"
    Logger.log(tag, msg)

    return this
}

fun <T> T.log(prefix: Any? = null): T {
    return this.log(Logger.TAG, prefix)
}