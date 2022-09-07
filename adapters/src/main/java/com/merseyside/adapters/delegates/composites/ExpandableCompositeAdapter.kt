package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.callback.OnItemExpandedListener
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.interfaces.expandable.ExpandableMode
import com.merseyside.adapters.interfaces.expandable.IExpandableAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ExpandableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ExpandableCompositeAdapter<Parent, Model, Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: DelegatesManager<Parent, Model> = DelegatesManager(),
    override var expandableMode: ExpandableMode = ExpandableMode.MULTIPLE
) : NestedCompositeAdapter<Parent, Model, Data, InnerAdapter>(scope, delegatesManager),
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