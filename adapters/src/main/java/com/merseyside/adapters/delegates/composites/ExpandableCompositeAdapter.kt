@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.delegates.composites

import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.base.SelectableAdapter
import com.merseyside.adapters.model.ExpandableAdapterViewModel
import com.merseyside.adapters.utils.ExpandableAdapterListUtils
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.view.TypedBindingHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ExpandableCompositeAdapter<Parent, Model : ExpandableAdapterViewModel<Parent, Data>,
        Data, InnerAdapter : BaseAdapter<Data, *>>(
    selectableMode: SelectableAdapter.SelectableMode = SelectableAdapter.SelectableMode.MULTIPLE,
    isAllowToCancelSelection: Boolean = true,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : SelectableCompositeAdapter<Parent, Model>(
    selectableMode = selectableMode,
    isAllowToCancelSelection = isAllowToCancelSelection,
    scope = scope
), ExpandableAdapterListUtils<Parent, Model, Data, InnerAdapter> {

    override var adapterList: MutableList<Pair<Model, InnerAdapter>> = ArrayList()

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        super.onBindViewHolder(holder, position)
        val model = getModelByPosition(position)

        val recyclerView: RecyclerView? = getExpandableView(holder.binding)
        recyclerView?.apply {
            val adapter = getExpandableAdapter(model)
            if (this.adapter != adapter) {
                this.adapter = adapter
            }
        }
    }

}