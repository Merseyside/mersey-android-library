package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.adapters.model.ExpandableAdapterViewModel
import com.merseyside.adapters.model.SelectableAdapterViewModel

open class SimpleCompositeAdapter: CompositeAdapter<Any, AdapterViewModel<Any>>()
open class SimpleSortedCompositeAdapter: SortedCompositeAdapter<Any, ComparableAdapterViewModel<Any>>()
open class SimpleSelectableCompositeAdapter: SelectableCompositeAdapter<Any, SelectableAdapterViewModel<Any>>()
abstract class SimpleExpandableCompositeAdapter<Data, InnerAdapter : BaseAdapter<Data, *>>:
    ExpandableCompositeAdapter<Any, ExpandableAdapterViewModel<Any, Data>, Data, InnerAdapter>()