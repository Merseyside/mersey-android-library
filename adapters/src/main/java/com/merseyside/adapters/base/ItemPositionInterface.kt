package com.merseyside.adapters.base


interface ItemPositionInterface<T> {

    fun getPosition(model: T): Int

    fun isLast(model: T): Boolean

    fun isFirst(model: T): Boolean
}