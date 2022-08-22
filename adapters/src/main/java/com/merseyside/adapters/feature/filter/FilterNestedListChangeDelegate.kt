package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.feature.filter.interfaces.FilterFeature
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.feature.filter.interfaces.NestedFilterFeature
import com.merseyside.adapters.listDelegates.NestedListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterNestedListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

class FilterNestedListChangeDelegate<Parent, Model, InnerData, InnerAdapter>(
    override val listChangeDelegate: NestedListChangeDelegate<Parent, Model, InnerData, InnerAdapter>,
    override val filterFeature: FilterFeature<Parent, Model>
) : FilterPrioritizedListChangeDelegate<Parent, Model>(listChangeDelegate, filterFeature),
    AdapterNestedListChangeDelegate<Parent, Model, InnerData, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    internal fun getNestedAdapterByModel(model: Model): InnerAdapter {
        return listChangeDelegate.getNestedAdapterByModel(model)
    }

    init {
        if (filterFeature is NestedFilterFeature<Parent, Model>) {
            filterFeature.getFilterableByModel = { model ->
                val nestedAdapter = getNestedAdapterByModel(model)
                if (nestedAdapter is Filterable<*, *>) nestedAdapter
                else null
            }
        }
    }

    override fun createModel(item: Parent): Model {
        return super.createModel(item).also { model ->
            if (filterFeature is NestedFilterFeature<Parent, Model>) {
                val filterable = getNestedAdapterByModel(model) as? Filterable<*, *>

                filterable?.let {
                    filterFeature.setFilters(filterable)
                }
            }
        }
    }
}