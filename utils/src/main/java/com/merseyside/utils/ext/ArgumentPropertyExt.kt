package com.merseyside.utils.ext

import com.merseyside.utils.delegate.ArgumentProperty
import kotlin.reflect.KProperty

fun <T, V, R> ArgumentProperty<T, V>.map(mapper: (V) -> R): ArgumentProperty<T, R> {
    return object : ArgumentProperty<T, R>(this.helper, key) {
        override fun getValue(thisRef: T, property: KProperty<*>): R {
            val value = this@map.getValue(thisRef, property)
            return mapper(value)
        }
    }
}