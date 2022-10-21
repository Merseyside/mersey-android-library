@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.NestedAdapterConfig
import com.merseyside.adapters.config.delegate
import com.merseyside.adapters.delegates.SimpleDelegatesManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.listManager.AdapterNestedListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class NestedCompositeAdapter<Parent, Model, Data, InnerAdapter>(
    final override val adapterConfig: NestedAdapterConfig<Parent, Model, Data, InnerAdapter> = NestedAdapterConfig(),
    delegatesManager: SimpleDelegatesManager<Parent, Model> = SimpleDelegatesManager()
) : SelectableCompositeAdapter<Parent, Model>(adapterConfig, delegatesManager),
    INestedAdapter<Parent, Model, Data, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, Data>,
              InnerAdapter : BaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

//    override val provideInnerAdapter: (Model) -> InnerAdapter = { model ->
//        getNestedAdapterByModel(model)
//    }

    override var adapterList: MutableList<Pair<Model, InnerAdapter>> = ArrayList()

    @InternalAdaptersApi
    override val delegate: AdapterNestedListManager<Parent, Model, Data, InnerAdapter> by adapterConfig.delegate()

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