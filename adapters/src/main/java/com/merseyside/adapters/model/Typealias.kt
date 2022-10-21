package com.merseyside.adapters.model

internal typealias VM<Parent> = AdapterParentViewModel<out Parent, Parent>

typealias AdapterViewModel<Item> = AdapterParentViewModel<Item, Item>
typealias SelectableAdapterViewModel<Item> = SelectableAdapterParentViewModel<Item, Item>
typealias NestedAdapterViewModel<Item, Data> = NestedAdapterParentViewModel<Item, Item, Data>
typealias ExpandableAdapterViewModel<Item, Data> = ExpandableAdapterParentViewModel<Item, Item, Data>