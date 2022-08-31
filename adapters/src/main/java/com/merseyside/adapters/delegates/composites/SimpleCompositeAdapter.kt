package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.interfaces.expandable.ExpandableMode
import com.merseyside.adapters.interfaces.selectable.SelectableMode
import com.merseyside.adapters.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class SimpleCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: DelegatesManager<Any, AdapterViewModel<Any>> = DelegatesManager()
) : CompositeAdapter<Any, AdapterViewModel<Any>>(scope, delegatesManager)

open class SimpleSortedCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: DelegatesManager<Any, ComparableAdapterViewModel<Any>> = DelegatesManager()
) : SortedCompositeAdapter<Any, ComparableAdapterViewModel<Any>>(scope, delegatesManager)

open class SimpleSelectableCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: DelegatesManager<Any, SelectableAdapterViewModel<Any>> = DelegatesManager(),
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
    delegatesManager: DelegatesManager<Any, NestedAdapterViewModel<Any, Data>> = DelegatesManager()
) : NestedCompositeAdapter<Any, NestedAdapterViewModel<Any, Data>, Data, InnerAdapter>(
    scope,
    delegatesManager
) where InnerAdapter : BaseAdapter<Data, *>

abstract class SimpleExpandableCompositeAdapter<Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: DelegatesManager<Any, ExpandableAdapterViewModel<Any, Data>> = DelegatesManager(),
    expandableMode: ExpandableMode = ExpandableMode.MULTIPLE
) : ExpandableCompositeAdapter<Any, ExpandableAdapterViewModel<Any, Data>, Data, InnerAdapter>(
    scope,
    delegatesManager,
    expandableMode
) where InnerAdapter : BaseAdapter<Data, *>