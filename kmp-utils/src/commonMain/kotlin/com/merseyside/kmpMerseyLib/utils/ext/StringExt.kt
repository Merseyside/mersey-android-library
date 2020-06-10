package com.merseyside.kmpMerseyLib.utils.ext

import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray

fun String.toUtf8(): String {
    val array = this.toByteArray(Charsets.UTF_8)
    return array.toString()
}