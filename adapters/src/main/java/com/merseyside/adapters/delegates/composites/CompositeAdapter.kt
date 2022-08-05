@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.interfaces.simple.ISimpleAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

open class CompositeAdapter<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    delegatesManager: DelegatesManager<Parent, Model> = DelegatesManager()
) : BaseCompositeAdapter<Parent, Model>(delegatesManager),
    ISimpleAdapter<Parent, Model>
{
    final override val mutModels: MutableList<Model> = ArrayList()
    override val models: List<Model> = mutModels

}