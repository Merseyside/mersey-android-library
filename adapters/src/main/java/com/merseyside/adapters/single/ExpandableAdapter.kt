package com.merseyside.adapters.single

import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.callback.OnItemExpandedListener
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.interfaces.expandable.ExpandableMode
import com.merseyside.adapters.interfaces.expandable.IExpandableAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ExpandableAdapterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ExpandableAdapter<Item, Model, Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob()),
    override var expandableMode: ExpandableMode = ExpandableMode.MULTIPLE
) : NestedAdapter<Item, Model, Data, InnerAdapter>(scope = scope),
    IExpandableAdapter<Item, Model, Data, InnerAdapter>
        where Model : ExpandableAdapterViewModel<Item, Data>,
              InnerAdapter :
              RecyclerView.Adapter<out TypedBindingHolder<out AdapterParentViewModel<out Data, Data>>>,
              InnerAdapter : IBaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

    override val expandedListeners: MutableList<OnItemExpandedListener<Item>> = ArrayList()

    override val internalExpandedCallback: (Model) -> Unit = { model ->
        if (model.isExpandable) {
            changeModelExpandedState(model)
        }
    }

}