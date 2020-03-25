package com.merseyside.merseyLib.utils.ext

fun String?.isNotNullAndEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}