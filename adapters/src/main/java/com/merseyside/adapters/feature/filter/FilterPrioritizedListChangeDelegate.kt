package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.feature.compare.validatePriority
import com.merseyside.adapters.listDelegates.PrioritizedListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPrioritizedListChangeDelegate
import com.merseyside.adapters.model.ComparableAdapterParentViewModel

open class FilterPrioritizedListChangeDelegate<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>(
    override val listChangeDelegate: PrioritizedListChangeDelegate<Parent, Model>,
    override val filterFeature: FilterFeature<Parent, Model>
) : FilterListChangeDelegate<Parent, Model>(filterFeature),
    AdapterPrioritizedListChangeDelegate<Parent, Model> {

    override fun add(item: Parent, priority: Int): Model {
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