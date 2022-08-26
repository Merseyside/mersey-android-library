package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class SimpleCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : CompositeAdapter<Any, AdapterViewModel<Any>>(scope)
open class SimpleSortedCompositeAdapter(scope: CoroutineScope = CoroutineScope(Dispatchers.Main)) :
    SortedCompositeAdapter<Any, ComparableAdapterViewModel<Any>>(scope)

open class SimpleSelectableCompositeAdapter(scope: CoroutineScope = CoroutineScope(Dispatchers.Main)) :
    SelectableCompositeAdapter<Any, SelectableAdapterViewModel<Any>>(scope)

abstract class SimpleNestedCompositeAdapter<Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : NestedCompositeAdapter<Any, NestedAdapterViewModel<Any, Data>, Data, InnerAdapter>(scope)
        where InnerAdapter : BaseAdapter<Data, *>

abstract class SimpleExpandableCompositeAdapter<Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : ExpandableCompositeAdapter<Any, ExpandableAdapterViewModel<Any, Data>, Data, InnerAdapter>(scope)
        where InnerAdapter : BaseAdapter<Data, *>