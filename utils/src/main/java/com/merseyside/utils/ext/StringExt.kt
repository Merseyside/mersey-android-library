package com.merseyside.utils.ext

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

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private val snakeRegex = "_[a-zA-Z]".toRegex()

// String extensions
fun String.camelToSnakeCase(): String {
    return camelRegex.replace(this) {
        "_${it.value}"
    }.toLowerCase()
}

fun String.snakeToLowerCamelCase(): String {
    return snakeRegex.replace(this) {
        it.value.replace("_","")
            .toUpperCase()
    }
}

fun String.snakeToUpperCamelCase(): String {
    return this.snakeToLowerCamelCase().capitalize()
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

fun String.getLettersCount() : Int {
    return filter { it.isLetter() }.count()
}