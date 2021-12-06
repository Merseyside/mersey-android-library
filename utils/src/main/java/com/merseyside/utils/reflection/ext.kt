package com.merseyside.utils.reflection

import kotlin.reflect.full.memberFunctions

fun Any.callMethodByName(name: String): Any? {
    return this::class.memberFunctions.single { it.name == name }.call(this)
}