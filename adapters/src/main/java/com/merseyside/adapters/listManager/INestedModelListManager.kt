package com.merseyside.adapters.listManager

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.nested.NestedAdapterActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

interface INestedModelListManager<Parent, Model, InnerData, InnerAdapter> :
    IModelListManager<Parent, Model>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    override val adapterActions: NestedAdapterActions<Parent, Model, InnerData, InnerAdapter>

    suspend fun provideNestedAdapter(model: Model): InnerAdapter {
        return adapterActions.getNestedAdapterByModel(model)
            ?: initNestedAdapterByModel(model)
    }

    suspend fun initNestedAdapterByModel(model: Model): InnerAdapter {
        return adapterActions.initNestedAdapterByModel(model)
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

    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        return super.updateModel(model, item).also {
            val adapter = provideNestedAdapter(model)
            model.getNestedData()?.let { data ->
                adapter.addOrUpdate(data)
            }
        }
    }

    override suspend fun createModel(item: Parent): Model {
        return super.createModel(item).also { model ->
            val adapter = provideNestedAdapter(model)
            model.getNestedData()?.let { data ->
                adapter.add(data)
            }
        }
    }
}