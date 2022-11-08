package com.merseyside.adapters.model

abstract class NestedAdapterParentViewModel<Item: Parent, Parent, Data>(
    item: Item,
    clickable: Boolean = true,
    deletable: Boolean = true,
    filterable: Boolean = true
) : AdapterParentViewModel<Item, Parent>(
    item, clickable, deletable, filterable
) {

    abstract fun getNestedData(): List<Data>?
}