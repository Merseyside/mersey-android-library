package com.merseyside.adapters.delegates.model

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ExpandableAdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

typealias SimpleAdapterViewModel<Item> = AdapterParentViewModel<Item, Any>
typealias SimpleNestedAdapterViewModel<Item, Data> = NestedAdapterParentViewModel<Item, Any, Data>
typealias SimpleExpandableAdapterViewModel<Item, Data> = ExpandableAdapterParentViewModel<Item, Any, Data>