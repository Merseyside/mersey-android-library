package com.merseyside.adapters.listDelegates.interfaces

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

interface AdapterNestedListChangeDelegate<Parent, Model, InnerData, InnerAdapter> :
    AdapterPrioritizedListChangeDelegate<Parent, Model>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>>