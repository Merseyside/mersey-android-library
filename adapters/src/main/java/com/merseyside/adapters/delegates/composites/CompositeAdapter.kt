@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.delegates.SimpleDelegatesManager
import com.merseyside.adapters.feature.filter.delegate.FilterPositionListChangeDelegate
import com.merseyside.adapters.interfaces.simple.ISimpleAdapter
import com.merseyside.adapters.listDelegates.PositionListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPositionListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.getFilter
import com.merseyside.adapters.utils.isFilterable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
open class CompositeAdapter<Parent, ParentModel>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: DelegatesManager<DelegateAdapter<out Parent, Parent, ParentModel>, Parent, ParentModel> = SimpleDelegatesManager()
) : BaseCompositeAdapter<Parent, ParentModel>(scope, delegatesManager), ISimpleAdapter<Parent, ParentModel>
    where ParentModel: AdapterParentViewModel<out Parent, Parent> {

    final override val mutModels: MutableList<ParentModel> = ArrayList()
    override val models: List<ParentModel> = mutModels

    override val defaultDelegate: PositionListChangeDelegate<Parent, ParentModel> by lazy {
        PositionListChangeDelegate(this)
    }

    override val filterDelegate: FilterPositionListChangeDelegate<Parent, ParentModel> by lazy {
        FilterPositionListChangeDelegate(defaultDelegate, getFilter())
    }

    override val delegate: AdapterPositionListChangeDelegate<Parent, ParentModel> by lazy {
        if (isFilterable()) filterDelegate else defaultDelegate
    }
}