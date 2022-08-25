@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.feature.filter.FilterPositionListChangeDelegate
import com.merseyside.adapters.interfaces.simple.ISimpleAdapter
import com.merseyside.adapters.listDelegates.PositionListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPositionListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.getFilter
import com.merseyside.adapters.utils.isFilterable

open class CompositeAdapter<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    delegatesManager: DelegatesManager<Parent, Model> = DelegatesManager()
) : BaseCompositeAdapter<Parent, Model>(delegatesManager), ISimpleAdapter<Parent, Model> {

    final override val mutModels: MutableList<Model> = ArrayList()
    override val models: List<Model> = mutModels

    override val defaultDelegate: PositionListChangeDelegate<Parent, Model> by lazy {
        PositionListChangeDelegate(this)
    }

    override val filterDelegate: FilterPositionListChangeDelegate<Parent, Model> by lazy {
        FilterPositionListChangeDelegate(workManager, defaultDelegate, getFilter())
    }

    override val delegate: AdapterPositionListChangeDelegate<Parent, Model> by lazy {
        if (isFilterable()) filterDelegate else defaultDelegate
    }
}