package com.merseyside.adapters.model

abstract class NestedAdapterParentViewModel<Item: Parent, Parent, Data>(
    item: Item,
    clickable: Boolean = true,
    deletable: Boolean = true,
    filterable: Boolean = true
) : SelectableAdapterParentViewModel<Item, Parent>(
    item, clickable, deletable, filterable
) {

    abstract fun getNestedData(): List<Data>?
}