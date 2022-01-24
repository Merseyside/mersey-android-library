@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.base

import com.merseyside.adapters.model.ExpandableAdapterViewModel
import com.merseyside.adapters.utils.ExpandableAdapterListUtils
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.view.TypedBindingHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ExpandableAdapter<Item, Model : ExpandableAdapterViewModel<Item, Data>,
        Data, InnerAdapter : BaseAdapter<Data, *>>(
    selectableMode: SelectableMode = SelectableMode.MULTIPLE,
    isAllowToCancelSelection: Boolean = true,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : SelectableAdapter<Item, Model>(selectableMode, isAllowToCancelSelection, scope = scope),
    ExpandableAdapterListUtils<Item, Model, Data, InnerAdapter> {

    override var adapterList: MutableList<Pair<Model, InnerAdapter>> = ArrayList()

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        super.onBindViewHolder(holder, position)
        val model = getModelByPosition(position)

        getExpandableView(holder.binding)?.apply {
            val adapter = getExpandableAdapter(model)
            if (this.adapter != adapter) {
                this.adapter = adapter
            }
        }
    }
}