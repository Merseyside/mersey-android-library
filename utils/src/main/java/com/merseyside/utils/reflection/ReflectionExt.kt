package com.merseyside.utils.reflection

import com.merseyside.merseyLib.kotlin.logger.log
import kotlin.reflect.full.memberFunctions

fun Any.callMethodByName(name: String): Any? {
    return this::class.memberFunctions.single { it.name == name }.call(this)
}

fun <T: Any> T.printMethodNames(tag: String? = null) {
    return this::class.memberFunctions.forEach { it.log(tag = tag ?: "Reflection") }
}