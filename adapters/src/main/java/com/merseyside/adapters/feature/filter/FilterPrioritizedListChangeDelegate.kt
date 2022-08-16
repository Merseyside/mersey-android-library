package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.feature.compare.validatePriority
import com.merseyside.adapters.feature.filter.interfaces.FilterFeature
import com.merseyside.adapters.interfaces.sorted.AdapterPrioritizedListActions
import com.merseyside.adapters.listDelegates.interfaces.AdapterPrioritizedListChangeDelegate
import com.merseyside.adapters.model.ComparableAdapterParentViewModel

class FilterPrioritizedListChangeDelegate<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>(
    override val listActions: AdapterPrioritizedListActions<Parent, Model>,
    filterFeature: FilterFeature<Parent, Model>
) : FilterListChangeDelegate<Parent, Model>(filterFeature),
    AdapterPrioritizedListChangeDelegate<Parent, Model> {

    override fun add(item: Parent, priority: Int): Model {
        validatePriority(priority)
        val model = createModel(item)
        if (!isFiltered()) {
            listActions.addModel(model, priority)
        } else {
            mutAllModelList.add(model)
            if (filterFeature.filter(model)) {
                listActions.addModel(model, priority)
            }
        }

        return model
    }
}