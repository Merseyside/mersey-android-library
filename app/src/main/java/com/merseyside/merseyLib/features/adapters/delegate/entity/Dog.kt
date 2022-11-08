package com.merseyside.merseyLib.features.adapters.delegate.entity


data class Dog(
    override val name: String,
    override val age: Int,
    val breed: String
): Animal(name, age)