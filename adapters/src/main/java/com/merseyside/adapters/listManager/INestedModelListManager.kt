package com.merseyside.adapters.listManager

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.nested.NestedAdapterActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

interface INestedModelListManager<Parent, Model, InnerData, InnerAdapter> :
    ModelListManager<Parent, Model>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    override val adapterActions: NestedAdapterActions<Parent, Model, InnerData, InnerAdapter>

    fun provideInnerAdapter(model: Model): InnerAdapter {
        return adapterActions.getNestedAdapterByModel(model)
    }

    override suspend fun remove(item: Parent): Model? {
        return super.remove(item)?.also { model ->
            removeNestedAdapterByModel(model)
        }
    }

    fun removeNestedAdapterByModel(model: Model): Boolean {
        adapterActions.removeNestedAdapterByModel(model)
        return true
    }

    override suspend fun createModel(item: Parent): Model {
        return super.createModel(item).also { model ->
            val adapter = provideInnerAdapter(model)
            val innerDataList = model.getNestedData()

            innerDataList?.let { data -> adapter.add(data) }
        }
    }
}