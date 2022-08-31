package com.merseyside.adapters.single

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.callback.OnItemExpandedListener
import com.merseyside.adapters.interfaces.expandable.ExpandableMode
import com.merseyside.adapters.interfaces.expandable.IExpandableAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ExpandableAdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ExpandableAdapter<Item, Model, Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob()),
    override var expandableMode: ExpandableMode = ExpandableMode.MULTIPLE
) : NestedAdapter<Item, Model, Data, InnerAdapter>(scope = scope),
    IExpandableAdapter<Item, Model, Data, InnerAdapter>
        where Model : ExpandableAdapterViewModel<Item, Data>,
              InnerAdapter : BaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

    override val expandedListeners: MutableList<OnItemExpandedListener<Item>> = ArrayList()

    @OptIn(InternalAdaptersApi::class)
    override val internalOnExpand: (Item) -> Unit = { item ->
        val model = getModelByItem(item)
        if (model.isExpandable) {
            changeModelExpandedState(model)
        }
    }

}