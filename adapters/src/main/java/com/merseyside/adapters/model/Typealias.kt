package com.merseyside.adapters.model

typealias AdapterViewModel<Item> = AdapterParentViewModel<Item, Item>
typealias ComparableAdapterViewModel<Item> = ComparableAdapterParentViewModel<Item, Item>
typealias SelectableAdapterViewModel<Item> = SelectableAdapterParentViewModel<Item, Item>
typealias ExpandableAdapterViewModel<Item, Data> = ExpandableAdapterParentViewModel<Item, Item, Data>