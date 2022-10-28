package com.merseyside.adapters.model

import com.merseyside.adapters.delegates.DelegateAdapter

internal typealias VM<Parent> = AdapterParentViewModel<out Parent, Parent>
internal typealias DA<Parent, ParentModel> = DelegateAdapter<out Parent, Parent, ParentModel>

typealias AdapterViewModel<Item> = AdapterParentViewModel<Item, Item>
typealias SelectableAdapterViewModel<Item> = SelectableAdapterParentViewModel<Item, Item>
typealias NestedAdapterViewModel<Item, Data> = NestedAdapterParentViewModel<Item, Item, Data>
typealias ExpandableAdapterViewModel<Item, Data> = ExpandableAdapterParentViewModel<Item, Item, Data>