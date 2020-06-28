package com.merseyside.utils.reflection

import com.merseyside.utils.ext.log
import kotlin.reflect.full.memberFunctions

fun Any.callMethodByName(name: String): Any? {
    return this::class.memberFunctions.single { it.name == name }.call(this)
}

inline fun <reified T: Any> T.printMethodNames() {
    return this::class.memberFunctions.forEach { it.log(tag = T::class.simpleName!!) }
}