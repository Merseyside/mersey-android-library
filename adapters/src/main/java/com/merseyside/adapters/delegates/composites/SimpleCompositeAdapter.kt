package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.delegates.SimpleDelegatesManager
import com.merseyside.adapters.interfaces.expandable.ExpandableMode
import com.merseyside.adapters.interfaces.selectable.SelectableMode
import com.merseyside.adapters.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class SimpleCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, AdapterParentViewModel<out Any, Any>> = SimpleDelegatesManager()
) : CompositeAdapter<Any, AdapterParentViewModel<out Any, Any>>(scope, delegatesManager)

open class SimpleSortedCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, ComparableAdapterParentViewModel<out Any, Any>> = SimpleDelegatesManager()
) : SortedCompositeAdapter<Any, ComparableAdapterParentViewModel<out Any, Any>>(scope, delegatesManager)

open class SimpleSelectableCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, SelectableAdapterParentViewModel<out Any, Any>> = SimpleDelegatesManager(),
    selectableMode: SelectableMode = SelectableMode.SINGLE,
    isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE,
    isSelectEnabled: Boolean = true
) : SelectableCompositeAdapter<Any, SelectableAdapterParentViewModel<out Any, Any>>(
    scope,
    delegatesManager,
    selectableMode,
    isAllowToCancelSelection,
    isSelectEnabled
)

abstract class SimpleNestedCompositeAdapter<Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, NestedAdapterParentViewModel<out Any, Any, Data>> = SimpleDelegatesManager()
) : NestedCompositeAdapter<Any, NestedAdapterParentViewModel<out Any, Any, Data>, Data, InnerAdapter>(
    scope,
    delegatesManager
) where InnerAdapter : BaseAdapter<Data, *>

abstract class SimpleExpandableCompositeAdapter<Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Any, ExpandableAdapterParentViewModel<out Any, Any, Data>> = SimpleDelegatesManager(),
    expandableMode: ExpandableMode = ExpandableMode.MULTIPLE
) : ExpandableCompositeAdapter<Any, ExpandableAdapterParentViewModel<out Any, Any, Data>, Data, InnerAdapter>(
    scope,
    delegatesManager,
    expandableMode
) where InnerAdapter : BaseAdapter<Data, *>