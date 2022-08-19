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

    init {
        if (filterFeature is NestedFilterFeature<Parent, Model>) {
            filterFeature.getFilterableByModel = { model ->
                val nestedAdapter = listChangeDelegate.getNestedAdapterByModel(model)
                if (nestedAdapter is Filterable<*, *>) nestedAdapter
                else null
            }
        }
    }


}