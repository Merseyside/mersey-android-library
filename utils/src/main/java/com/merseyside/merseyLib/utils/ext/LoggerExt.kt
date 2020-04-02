package com.merseyside.merseyLib.utils.ext

import com.merseyside.merseyLib.utils.Logger

fun <T> T.log(tag: Any = Logger.TAG, prefix: Any? = null): T {
    val msg = if (prefix == null) {
        this
    } else {
        "$prefix $this"
    }

    Logger.log(tag, msg)

    return this
}