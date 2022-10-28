package com.merseyside.adapters.listManager.impl

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.nested.NestedAdapterActions
import com.merseyside.adapters.listManager.INestedModelListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.modelList.ModelList

class NestedModelListManager<Parent, Model, InnerData, InnerAdapter>(
    modelList: ModelList<Parent, Model>,
    override val adapterActions: NestedAdapterActions<Parent, Model, InnerData, InnerAdapter>,
) : ListManager<Parent, Model>(modelList, adapterActions),
    INestedModelListManager<Parent, Model, InnerData, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>>