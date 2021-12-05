package com.merseyside.utils.ext

import kotlin.reflect.full.memberFunctions

inline fun <reified T: Any> T.printMethodNames() {
    return this::class.memberFunctions.forEach { it.log(tag = T::class.simpleName!!) }
}