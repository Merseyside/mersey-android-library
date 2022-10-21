package com.merseyside.adapters.feature.filtering.listManager

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.base.config.ext.getAdapterFilter
import com.merseyside.adapters.feature.filtering.AdapterFilter
import com.merseyside.adapters.feature.filtering.NestedAdapterFilter
import com.merseyside.adapters.interfaces.nested.AdapterNestedListActions
import com.merseyside.adapters.listManager.AdapterNestedListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.modelList.ModelList

class FilterNestedListManager<Parent, Model, InnerData, InnerAdapter>(
    modelList: ModelList<Parent, Model>,
    override val listActions: AdapterNestedListActions<Parent, Model, InnerData, InnerAdapter>,
    adapterFilter: AdapterFilter<Parent, Model>,
) : FilterListManager<Parent, Model>(modelList, listActions, adapterFilter),
    AdapterNestedListManager<Parent, Model, InnerData, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    private fun getInnerAdapterFilter(model: Model): AdapterFilter<InnerData, *>? {
        val innerAdapter = provideInnerAdapter(model)
        return innerAdapter.adapterConfig.getAdapterFilter()
    }

    init {
        if (adapterFilter is NestedAdapterFilter<Parent, Model>) {
            adapterFilter.getAdapterFilterByModel = { model ->
                getInnerAdapterFilter(model)
            }
        }
    }
}