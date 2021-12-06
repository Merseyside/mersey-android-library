package com.merseyside.utils.reflection

import kotlin.reflect.KCallable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

interface ReflectionHelper {

    fun set(name: String, value: Any) {
        val property = getPropertyByName(name)

        if (property != null) {
            return if (property is KMutableProperty<*>) {
                property.setter.call(this, value)
            } else {
                throw IllegalAccessException()
            }
        } else {
            throw KotlinNullPointerException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(name: String): T? {
        val property = getPropertyByName(name)

        if (property != null) {
            return if (property is KProperty) {
                property.getter.call(this) as T
            } else {
                throw IllegalAccessException()
            }
        } else {
            throw KotlinNullPointerException()
        }
    }

    fun getPropertyByName(name: String): KCallable<*>? {
        return this::class.members.find { it.name == name }
    }

    fun getConstructorFields(isSorted: Boolean = true): List<String>? {
        return this::class.primaryConstructor?.parameters
            ?.mapNotNull { it.name }
            .also {
                if (!isSorted) {
                    it?.sorted()
                }
            }
    }

    fun getMemberNames(): List<String> {

        return this::class.declaredMemberProperties.map {
            val name = it.name

            name
//            if (
//                !name.startsWith("component") &&
//                name != "hashCode" &&
//                name != "equals" &&
//                name != "set" &&
//                name != "get" &&
//                name != "copy" &&
//                name != "toString" &&
//                name != "getMemberNames" &&
//                name != "getPropertyByName" &&
//                name != "getConstructorFields")
//            {
//                name
//            } else {
//                null
//            }
        }
    }
}

