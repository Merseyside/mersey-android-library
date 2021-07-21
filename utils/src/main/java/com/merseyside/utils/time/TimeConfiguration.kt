package com.merseyside.utils.time


object TimeConfiguration {

    var timeZone: String = SYSTEM.toString()

    var language: Language = "en"
    var country: Country = "US"


    var formatPatterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyy-MM-dd'T'HH:mm:ssZ"
    )

    fun addFormatPattern(pattern: String) {
        formatPatterns = formatPatterns.toMutableList().apply { add(pattern) }
    }
}

typealias Country = String
typealias Language = String