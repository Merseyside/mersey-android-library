package com.merseyside.adapters.interfaces.nested

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.sorted.AdapterPrioritizedListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

interface AdapterNestedListActions<Parent, Model, InnerData, InnerAdapter>
    : AdapterPrioritizedListActions<Parent, Model>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    fun getNestedAdapterByModel(model: Model): InnerAdapter
}