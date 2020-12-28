package com.merseyside.adapters.base

interface ItemPositionInterface<T> {

    fun getPosition(model: T): Int

    fun getItemCount(): Int
}