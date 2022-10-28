package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.NestedAdapterConfig
import com.merseyside.adapters.callback.OnItemExpandedListener
import com.merseyside.adapters.delegates.SimpleDelegatesManager
import com.merseyside.adapters.interfaces.expandable.ExpandableMode
import com.merseyside.adapters.interfaces.expandable.IExpandableAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ExpandableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class ExpandableComposite<Parent, Model, Data, InnerAdapter>(
    adapterConfig: NestedAdapterConfig<Parent, Model, Data, InnerAdapter> = NestedAdapterConfig(),
    delegatesManager: SimpleDelegatesManager<Parent, Model> = SimpleDelegatesManager(),
    override var expandableMode: ExpandableMode = ExpandableMode.MULTIPLE
) : NestedCompositeAdapter<Parent, Model, Data, InnerAdapter>(adapterConfig, delegatesManager),
    IExpandableAdapter<Parent, Model, Data, InnerAdapter>
        where Model : ExpandableAdapterParentViewModel<out Parent, Parent, Data>,
              InnerAdapter : BaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

    override val expandedListeners: MutableList<OnItemExpandedListener<Parent>> = ArrayList()

    @OptIn(InternalAdaptersApi::class)
    override val internalOnExpand: (Parent) -> Unit = { item ->
        doAsync {
            val model = getModelByItem(item)
            model?.let {
                if (model.isExpandable) {
                    changeModelExpandedState(model)
                }
            }
        }
    }

}