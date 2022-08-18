package com.merseyside.adapters.delegates.composites

import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.*

open class SimpleCompositeAdapter : CompositeAdapter<Any, AdapterViewModel<Any>>()
open class SimpleSortedCompositeAdapter :
    SortedCompositeAdapter<Any, ComparableAdapterViewModel<Any>>()

open class SimpleSelectableCompositeAdapter :
    SelectableCompositeAdapter<Any, SelectableAdapterViewModel<Any>>()

abstract class SimpleNestedCompositeAdapter<Data, InnerAdapter> :
    NestedCompositeAdapter<Any, NestedAdapterViewModel<Any, Data>, Data, InnerAdapter>()
        where InnerAdapter : IBaseAdapter<Data, *>,
              InnerAdapter : RecyclerView.Adapter<out TypedBindingHolder<out AdapterParentViewModel<out Data, Data>>>

abstract class SimpleExpandableCompositeAdapter<Data, InnerAdapter> :
    ExpandableCompositeAdapter<Any, ExpandableAdapterViewModel<Any, Data>, Data, InnerAdapter>()
        where InnerAdapter : IBaseAdapter<Data, *>,
              InnerAdapter : RecyclerView.Adapter<out TypedBindingHolder<out AdapterParentViewModel<out Data, Data>>>