package com.merseyside.adapters.feature.filter.delegate

import com.merseyside.adapters.feature.compare.Priority.validatePriority
import com.merseyside.adapters.feature.filter.FilterFeature
import com.merseyside.adapters.listDelegates.PrioritizedListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPrioritizedListChangeDelegate
import com.merseyside.adapters.model.ComparableAdapterParentViewModel

open class FilterPrioritizedListChangeDelegate<Parent, Model>(
    override val listChangeDelegate: PrioritizedListChangeDelegate<Parent, Model>,
    override val filterFeature: FilterFeature<Parent, Model>
) : FilterListChangeDelegate<Parent, Model>(filterFeature),
    AdapterPrioritizedListChangeDelegate<Parent, Model>
        where Model : ComparableAdapterParentViewModel<out Parent, Parent> {

    override suspend fun add(item: Parent, priority: Int): Model {
        validatePriority(priority)
        val model = createModel(item)
        if (!isFiltered()) {
            listChangeDelegate.addModel(model, priority)
        } else {
            mutAllModelList.add(model)
            if (filterFeature.filter(model)) {
                listChangeDelegate.addModel(model, priority)
            }
        }

        return model
    }
}