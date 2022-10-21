package com.merseyside.adapters.listManager

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.nested.AdapterNestedListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

interface AdapterNestedListManager<Parent, Model, InnerData, InnerAdapter> :
    AdapterListManager<Parent, Model>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    override val listActions: AdapterNestedListActions<Parent, Model, InnerData, InnerAdapter>

    fun provideInnerAdapter(model: Model): InnerAdapter {
        return listActions.getNestedAdapterByModel(model)
    }

    override suspend fun remove(item: Parent): Model? {
        return super.remove(item)?.also { model ->
            removeNestedAdapterByModel(model)
        }
    }

    fun removeNestedAdapterByModel(model: Model): Boolean {
        listActions.removeNestedAdapterByModel(model)
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