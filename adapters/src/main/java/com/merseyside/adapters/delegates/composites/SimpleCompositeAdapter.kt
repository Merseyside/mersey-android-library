package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.model.*

open class SimpleCompositeAdapter : CompositeAdapter<Any, AdapterViewModel<Any>>()
open class SimpleSortedCompositeAdapter :
    SortedCompositeAdapter<Any, ComparableAdapterViewModel<Any>>()

open class SimpleSelectableCompositeAdapter :
    SelectableCompositeAdapter<Any, SelectableAdapterViewModel<Any>>()

abstract class SimpleNestedCompositeAdapter<Data, InnerAdapter> :
    NestedCompositeAdapter<Any, NestedAdapterViewModel<Any, Data>, Data, InnerAdapter>()
        where InnerAdapter : BaseAdapter<Data, *>

abstract class SimpleExpandableCompositeAdapter<Data, InnerAdapter> :
    ExpandableCompositeAdapter<Any, ExpandableAdapterViewModel<Any, Data>, Data, InnerAdapter>()
        where InnerAdapter : BaseAdapter<Data, *>