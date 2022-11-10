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

class FilterNestedModelListManager<Parent, Model, InnerData, InnerAdapter>(
    modelList: ModelList<Parent, Model>,
    override val adapterActions: NestedAdapterActions<Parent, Model, InnerData, InnerAdapter>,
    adapterFilter: AdapterFilter<Parent, Model>,
) : FilterModelListManager<Parent, Model>(modelList, adapterActions, adapterFilter),
    INestedModelListManager<Parent, Model, InnerData, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    init {
        if (adapterFilter is NestedAdapterFilter<Parent, Model>) {
            adapterFilter.getAdapterFilterByModel = { model ->
                getInnerAdapterFilter(model)
            }
        }
    }

    override suspend fun initNestedAdapterByModel(model: Model): InnerAdapter {
        return super.initNestedAdapterByModel(model).also { adapter ->
            if (adapterFilter is NestedAdapterFilter<Parent, Model>) {
                adapter.adapterConfig.getAdapterFilter()?.let { innerAdapterFilter ->
                    adapterFilter.initAdapterFilter(innerAdapterFilter)
                }
            }
        }
    }

    private suspend fun getInnerAdapterFilter(model: Model): AdapterFilter<InnerData, *>? {
        val innerAdapter = provideNestedAdapter(model)
        return innerAdapter.adapterConfig.getAdapterFilter()
    }

    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        return super<INestedModelListManager>.updateModel(model, item).also {
            if (isFiltered) {
                val filtered = adapterFilter.filter(model)
                if (!filtered) {
                    removeModel(model)
                }
            }
        }
    }
}