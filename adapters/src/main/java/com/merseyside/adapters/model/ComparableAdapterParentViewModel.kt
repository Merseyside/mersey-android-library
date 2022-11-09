package com.merseyside.adapters.model

abstract class ComparableAdapterParentViewModel<Item : Parent, Parent>(
    item: Item,
    clickable: Boolean = true,
    deletable: Boolean = true,
    filterable: Boolean = true
) : AdapterParentViewModel<Item, Parent>(item, clickable, deletable, filterable) {

    internal var priority: Int = 0

    open fun compareTo(other: Parent): Int {
        throw NotImplementedError("Set your custom comparator or override this method!")
    }
}
