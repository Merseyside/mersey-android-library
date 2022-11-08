package com.merseyside.adapters.callback

import com.merseyside.adapters.utils.InternalAdaptersApi

interface OnItemClickListener<Item> {

    fun onItemClicked(item: Item)
}

interface HasOnItemClickListener<Item> {
    val clickListeners: MutableList<OnItemClickListener<Item>>

    fun setOnItemClickListener(listener: OnItemClickListener<Item>) {
        clickListeners.add(listener)
    }

    fun removeOnItemClickListener(listener: OnItemClickListener<Item>) {
        clickListeners.remove(listener)
    }

    @InternalAdaptersApi
    fun notifyOnClick(item: Item) {
        clickListeners.forEach { listener -> listener.onItemClicked(item) }
    }

    fun removeAllClickListeners() {
        clickListeners.clear()
    }
}