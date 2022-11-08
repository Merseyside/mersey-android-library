package com.merseyside.adapters.feature.filtering.listManager

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.ext.getAdapterFilter
import com.merseyside.adapters.feature.filtering.AdapterFilter
import com.merseyside.adapters.feature.filtering.NestedAdapterFilter
import com.merseyside.adapters.interfaces.nested.NestedAdapterActions
import com.merseyside.adapters.listManager.INestedModelListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.modelList.ModelList

class FilterINestedModelListManager<Parent, Model, InnerData, InnerAdapter>(
    modelList: ModelList<Parent, Model>,
    override val adapterActions: NestedAdapterActions<Parent, Model, InnerData, InnerAdapter>,
    adapterFilter: AdapterFilter<Parent, Model>,
) : FilterListManager<Parent, Model>(modelList, adapterActions, adapterFilter),
    INestedModelListManager<Parent, Model, InnerData, InnerAdapter>
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