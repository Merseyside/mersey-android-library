package com.merseyside.merseyLib.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

fun isIpValid(host: String): Boolean {

    val pattern: Pattern = Pattern.compile(IPADDRESS_PATTERN)
    val matcher: Matcher = pattern.matcher(host)
    return matcher.matches()
}

private val IPADDRESS_PATTERN =
    "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"