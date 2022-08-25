package com.merseyside.adapters.model

abstract class ComparableAdapterParentViewModel<Item : Parent, Parent>(item: Item) :
    AdapterParentViewModel<Item, Parent>(item) {

    internal var priority: Int = 0

    open fun compareTo(other: Parent): Int {
        throw NotImplementedError("Set your custom comparator or override this method!")
    }
}
