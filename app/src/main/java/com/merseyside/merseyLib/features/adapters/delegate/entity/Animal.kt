package com.merseyside.merseyLib.features.adapters.delegate.entity

import com.merseyside.merseyLib.kotlin.contract.Identifiable

abstract class Animal(
    open val name: String,
    open val age: Int
): Identifiable<String> {

    override fun getId(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Animal) return false

        if (name != other.name) return false
        if (age != other.age) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age
        return result
    }
}