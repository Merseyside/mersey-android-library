package com.merseyside.adapters.delegates

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.model.ExpandableAdapterParentViewModel
import com.merseyside.adapters.model.SelectableAdapterParentViewModel

typealias SimpleDelegateAdapter<Item, Model> = DelegateAdapter<Item, Any, Model>

typealias SimpleAdapterViewModel <Item> = AdapterParentViewModel<Item, Any>
typealias SimpleComparableAdapterViewModel<Item> = ComparableAdapterParentViewModel<Item, Any>
typealias SimpleSelectableAdapterViewModel<Item> = SelectableAdapterParentViewModel<Item, Any>
typealias SimpleExpandableAdapterViewModel<Item, Data> = ExpandableAdapterParentViewModel<Item, Any, Data>