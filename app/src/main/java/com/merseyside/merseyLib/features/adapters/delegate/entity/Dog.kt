package com.merseyside.merseyLib.features.adapters.delegate.entity

class Dog(
    name: String,
    age: Int,
    val breed: String
): Animal(name, age) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Dog) return false
        if (!super.equals(other)) return false

        if (breed != other.breed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + breed.hashCode()
        return result
    }
}