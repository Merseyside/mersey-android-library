package com.merseyside.adapters.model

abstract class NestedAdapterParentViewModel<Item: Parent, Parent, Data>(
    item: Item,
) : ComparableAdapterParentViewModel<Item, Parent>(item) {

    abstract fun getNestedData(): List<Data>?
}