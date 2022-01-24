package com.merseyside.adapters.model

abstract class ComparableAdapterParentViewModel<Item: Parent, Parent>(item: Item)
    : AdapterParentViewModel<Item, Parent>(item) {

    internal fun areContentsTheSame(parent: Parent): Boolean {
        return try {
            val another = parent as Item
            areContentsTheSame(another)
        } catch (e: ClassCastException) {
            areParentContentsTheSame(parent)
        }
    }

    open fun areParentContentsTheSame(parent: Parent): Boolean {
        return this.item == parent
    }

    abstract fun areContentsTheSame(other: Item): Boolean
    abstract fun compareTo(other: Parent) : Int

    open fun payload(newItem: Item): List<Payloadable> {
        this.item = newItem
        notifyUpdate()
        return listOf(Payloadable.None)
    }

    interface Payloadable {
        object None: Payloadable
    }

    inline fun <reified T> Any?.tryCast(block: T.() -> Unit) {
        if (this is T) {
            block()
        }
    }
}
