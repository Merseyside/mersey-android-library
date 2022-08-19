package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.feature.filter.interfaces.FilterFeature
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.feature.filter.interfaces.NestedFilterFeature
import com.merseyside.adapters.interfaces.nested.AdapterNestedListActions
import com.merseyside.adapters.listDelegates.interfaces.AdapterNestedListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.NestedListChangeDelegate
import com.merseyside.adapters.model.NestedAdapterParentViewModel

class FilterNestedListChangeDelegate<Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, *>, InnerAdapter>(
    override val listChangeDelegate: NestedListChangeDelegate<Parent, Model, InnerAdapter>,
    override val filterFeature: FilterFeature<Parent, Model>
) : FilterPrioritizedListChangeDelegate<Parent, Model>(listChangeDelegate, filterFeature),
    AdapterNestedListChangeDelegate<Parent, Model, InnerAdapter> {

    override val listActions: AdapterNestedListActions<Parent, Model, InnerAdapter>
        get() = listChangeDelegate.listActions

    init {
        if (filterFeature is NestedFilterFeature<Parent, Model>) {
            filterFeature.getFilterableByModel = { model ->
                val nestedAdapter = listActions.getNestedAdapterByModel(model)
                if (nestedAdapter is Filterable<*, *>) nestedAdapter
                else null
            }
        }
    }


}