package com.merseyside.adapters.delegates

import com.merseyside.adapters.model.*

typealias SimpleDelegateAdapter<Item, Model> = DelegateAdapter<Item, Any, Model>
typealias SimplePrioritizedDelegateAdapter<Item, Model> = PrioritizedDelegateAdapter<Item, Any, Model>
typealias SimpleNestedDelegateAdapter<Item, Model, Data, InnerAdapter> = NestedDelegateAdapter<Item, Any, Model, Data, InnerAdapter>

typealias SimpleAdapterViewModel<Item> = AdapterParentViewModel<Item, Any>
typealias SimpleComparableAdapterViewModel<Item> = ComparableAdapterParentViewModel<Item, Any>
typealias SimpleSelectableAdapterViewModel<Item> = SelectableAdapterParentViewModel<Item, Any>
typealias SimpleNestedAdapterViewModel<Item, Data> = NestedAdapterParentViewModel<Item, Any, Data>
typealias SimpleExpandableAdapterViewModel<Item, Data> = ExpandableAdapterParentViewModel<Item, Any, Data>