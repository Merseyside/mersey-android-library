package com.merseyside.adapters.listDelegates

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.nested.AdapterNestedListActions
import com.merseyside.adapters.listDelegates.interfaces.AdapterNestedListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

class NestedListChangeDelegate<Parent, Model, InnerData, InnerAdapter>(
    override val listActions: AdapterNestedListActions<Parent, Model, InnerData, InnerAdapter>
) : PrioritizedListChangeDelegate<Parent, Model>(listActions),
    AdapterNestedListChangeDelegate<Parent, Model, InnerData, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    internal fun getNestedAdapterByModel(model: Model): InnerAdapter {
        return listActions.getNestedAdapterByModel(model)
    }

    override fun createModel(item: Parent): Model {
        return super.createModel(item).also { model ->
            val adapter = getNestedAdapterByModel(model)
            val innerDataList = model.getNestedData()

            innerDataList?.let { data -> adapter.add(data) }
        }
    }

    override fun updateModel(model: Model, item: Parent): Boolean {
        val isUpdated = super.updateModel(model, item)
        if (isUpdated) {
            val adapter = getNestedAdapterByModel(model)
            val innerDataList = model.getNestedData()
            innerDataList?.let { data -> adapter.update(data) }
        }

        return isUpdated
    }
}