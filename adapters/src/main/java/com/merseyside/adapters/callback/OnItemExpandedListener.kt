package com.merseyside.adapters.callback

interface OnItemExpandedListener<Item> {

    fun onExpanded(item: Item, isExpanded: Boolean, isExpandedByUser: Boolean)
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
        expandedListeners.forEach { it.onExpanded(item, isExpanded, isExpandedByUser) }
    }

    fun notifyOnExpanded(item: Item, isExpanded: Boolean, isExpandedByUser: Boolean) {
        expandedListeners.forEach { listener ->
            listener.onExpanded(item, isExpanded, isExpandedByUser)
        }
    }
}