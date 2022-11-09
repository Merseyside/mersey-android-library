package com.merseyside.adapters.callback

interface OnItemExpandedListener<Item> {

    fun onExpandedStateChanged(item: Item, isExpanded: Boolean, isExpandedByUser: Boolean)
}

interface HasOnItemExpandedListener<Item> {
    val expandedListeners: MutableList<OnItemExpandedListener<Item>>

    fun addOnItemExpandedListener(listener: OnItemExpandedListener<Item>) {
        expandedListeners.add(listener)
    }

    fun removeOnItemExpandedListener(listener: OnItemExpandedListener<Item>) {
        expandedListeners.remove(listener)
    }

    fun notifyAllExpandedListeners(item: Item, isExpanded: Boolean, isExpandedByUser: Boolean) {
        expandedListeners.forEach { it.onExpandedStateChanged(item, isExpanded, isExpandedByUser) }
    }
}