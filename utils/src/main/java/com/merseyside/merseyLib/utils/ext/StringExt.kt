package com.merseyside.merseyLib.utils.ext

fun String?.isNotNullAndEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}

fun String.trimTrailingZero(): String {

    return if (this.isNotEmpty()) {
        if (this.indexOf(".") < 0) {
            this

        } else {
            this.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
        }

    } else {
        this
    }
}