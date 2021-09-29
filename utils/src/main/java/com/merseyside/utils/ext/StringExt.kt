package com.merseyside.utils.ext

import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun String?.isNotNullAndEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndEmpty != null)
    }

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

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private val snakeRegex = "_[a-zA-Z]".toRegex()

// String extensions
fun String.camelToSnakeCase(): String {
    return camelRegex.replace(this) {
        "_${it.value}"
    }.lowercase()
}

fun String.snakeToLowerCamelCase(): String {
    return snakeRegex.replace(this) {
        it.value.replace("_", "")
            .uppercase()
    }
}

fun String.snakeToUpperCamelCase(): String {
    return this.snakeToLowerCamelCase().capitalize(Locale.getDefault())
}

fun String.camelToHumanReadable(): String {
    return this.camelToSnakeCase().replace("_", " ")
}

fun String.capitalLettersCount(): Int {
    return toCharArray().filter { it.isUpperCase() }.size
}

fun String.lowerLettersCount(): Int {
    return toCharArray().filter { it.isLowerCase() }.size
}

fun String.containsUpperCase(): Boolean {
    return toCharArray().find { it.isUpperCase() } != null
}

fun String.containsLowerCase(): Boolean {
    return toCharArray().find { it.isLowerCase() } != null
}

fun String.startsWithUpperCase(): Boolean {
    return if (isNotEmpty()) {
        this[0].isUpperCase()
    } else {
        false
    }
}

fun String.startWithUpperCase(): String {
    return substring(0, 1).uppercase() + substring(1)
}

fun String.startsWithLowerCase(): Boolean {
    return if (isNotEmpty()) {
        this[0].isLowerCase()
    } else {
        false
    }
}

fun String.containsDigits(): Boolean {
    return contains("[0-9]".toRegex())
}

fun String.getLettersCount(): Int {
    return filter { it.isLetter() }.count()
}

fun String.replace(vararg oldValues: String, newValue: String): String {
    var newString = this
    oldValues.forEach {
        newString = newString.replace(it, newValue)
    }

    return newString
}