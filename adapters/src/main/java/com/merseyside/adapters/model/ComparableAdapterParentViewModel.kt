package com.merseyside.adapters.model

abstract class ComparableAdapterParentViewModel<Item : Parent, Parent>(item: Item) :
    AdapterParentViewModel<Item, Parent>(item) {

    abstract fun compareTo(other: Parent): Int
}
