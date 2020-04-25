package com.merseyside.merseyLib.adapters


interface ItemPositionInterface<T> {

    fun getPosition(model: T): Int

    fun isLast(model: T): Boolean

    fun isFirst(model: T): Boolean
}