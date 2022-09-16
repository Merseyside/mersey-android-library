package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.delegates.SimpleDelegatesManager
import com.merseyside.adapters.interfaces.expandable.ExpandableMode
import com.merseyside.adapters.interfaces.selectable.SelectableMode
import com.merseyside.adapters.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class SimpleCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, AdapterViewModel<Any>> = SimpleDelegatesManager()
) : CompositeAdapter<Any, AdapterViewModel<Any>>(scope, delegatesManager)

open class SimpleSortedCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, ComparableAdapterViewModel<Any>> = SimpleDelegatesManager()
) : SortedCompositeAdapter<Any, ComparableAdapterViewModel<Any>>(scope, delegatesManager)

open class SimpleSelectableCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, SelectableAdapterViewModel<Any>> = SimpleDelegatesManager(),
    selectableMode: SelectableMode = SelectableMode.SINGLE,
    isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE,
    isSelectEnabled: Boolean = true
) : SelectableCompositeAdapter<Any, SelectableAdapterViewModel<Any>>(
    scope,
    delegatesManager,
    selectableMode,
    isAllowToCancelSelection,
    isSelectEnabled
)

abstract class SimpleNestedCompositeAdapter<Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, NestedAdapterViewModel<Any, Data>> = SimpleDelegatesManager()
) : NestedCompositeAdapter<Any, NestedAdapterViewModel<Any, Data>, Data, InnerAdapter>(
    scope,
    delegatesManager
) where InnerAdapter : BaseAdapter<Data, *>

abstract class SimpleExpandableCompositeAdapter<Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, ExpandableAdapterViewModel<Any, Data>> = SimpleDelegatesManager(),
    expandableMode: ExpandableMode = ExpandableMode.MULTIPLE
) : ExpandableCompositeAdapter<Any, ExpandableAdapterViewModel<Any, Data>, Data, InnerAdapter>(
    scope,
    delegatesManager,
    expandableMode
) where InnerAdapter : BaseAdapter<Data, *>