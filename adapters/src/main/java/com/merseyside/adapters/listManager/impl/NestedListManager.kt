package com.merseyside.adapters.listManager.impl

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.nested.AdapterNestedListActions
import com.merseyside.adapters.listManager.AdapterNestedListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.modelList.ModelList

class NestedListManager<Parent, Model, InnerData, InnerAdapter>(
    modelList: ModelList<Parent, Model>,
    override val listActions: AdapterNestedListActions<Parent, Model, InnerData, InnerAdapter>,
) : ListManager<Parent, Model>(modelList, listActions),
    AdapterNestedListManager<Parent, Model, InnerData, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>>