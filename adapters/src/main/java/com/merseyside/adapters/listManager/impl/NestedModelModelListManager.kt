package com.merseyside.adapters.listManager.impl

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.nested.NestedAdapterActions
import com.merseyside.adapters.listManager.INestedModelListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.utils.AdapterWorkManager

class NestedModelModelListManager<Parent, Model, InnerData, InnerAdapter>(
    modelList: ModelList<Parent, Model>,
    override val adapterActions: NestedAdapterActions<Parent, Model, InnerData, InnerAdapter>,
    workManager: AdapterWorkManager
) : ModelListManager<Parent, Model>(modelList, adapterActions, workManager),
    INestedModelListManager<Parent, Model, InnerData, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        return super<INestedModelListManager>.updateModel(model, item)
    }
}