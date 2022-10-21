@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.single

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.NestedAdapterConfig
import com.merseyside.adapters.config.delegate
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.listManager.AdapterNestedListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class NestedAdapter<Item, Model, Data, InnerAdapter>(
    adapterConfig: NestedAdapterConfig<Item, Model, Data, InnerAdapter> = NestedAdapterConfig(),
) : SelectableAdapter<Item, Model>(adapterConfig),
    INestedAdapter<Item, Model, Data, InnerAdapter>
        where Model : NestedAdapterViewModel<Item, Data>,
              InnerAdapter : BaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

    override var adapterList: MutableList<Pair<Model, InnerAdapter>> = ArrayList()

    @InternalAdaptersApi
    override val delegate: AdapterNestedListManager<Item, Model, Data, InnerAdapter> by adapterConfig.delegate()

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        super.onBindViewHolder(holder, position)
        val model = getModelByPosition(position)

        getNestedView(holder.binding)?.apply {
            val adapter = getNestedAdapterByModel(model)
            if (this.adapter != adapter) {
                this.adapter = adapter
            }
        }
    }
}