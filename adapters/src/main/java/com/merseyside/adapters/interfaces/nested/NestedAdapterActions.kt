package com.merseyside.adapters.interfaces.nested

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.base.AdapterActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

interface NestedAdapterActions<Parent, Model, InnerData, InnerAdapter>
    : AdapterActions<Parent, Model>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    fun getNestedAdapterByModel(model: Model): InnerAdapter

    fun removeNestedAdapterByModel(model: Model): Boolean
}