package com.merseyside.adapters.model

abstract class ComparableAdapterParentViewModel<Item : Parent, Parent>(item: Item) :
    AdapterParentViewModel<Item, Parent>(item) {

    internal fun areContentsTheSame(parent: Parent): Boolean {
        val another = parent as Item
        return areContentsTheSame(another)
    }

    abstract fun areContentsTheSame(other: Item): Boolean
    abstract fun compareTo(other: Parent): Int

//    inline fun <reified T> Any?.tryCast(block: T.() -> Unit) {
//        if (this is T) {
//            block()
//        }
//    }
}
