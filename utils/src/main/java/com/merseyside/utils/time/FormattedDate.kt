package com.merseyside.utils.time

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class FormattedDate(val value: String) {

    companion object {
        fun empty(): FormattedDate = FormattedDate("")
    }
}

fun FormattedDate.isEmpty() = value.isEmpty()