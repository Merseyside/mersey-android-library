package com.merseyside.merseyLib.utils.ext

import com.merseyside.merseyLib.utils.Logger

fun <T> T.log(tag: Any = Logger.TAG, msg: Any? = this): T {
    Logger.log(tag, msg)

    return this
}