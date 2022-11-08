package com.merseyside.adapters.single

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.NestedAdapterConfig
import com.merseyside.adapters.config.listManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.interfaces.nested.OnInitNestedAdapterListener
import com.merseyside.adapters.listManager.INestedModelListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class NestedAdapter<Item, Model, Data, InnerAdapter>(
    adapterConfig: NestedAdapterConfig<Item, Model, Data, InnerAdapter> = NestedAdapterConfig(),
) : SimpleAdapter<Item, Model>(adapterConfig),
    INestedAdapter<Item, Model, Data, InnerAdapter>
        where Model : NestedAdapterViewModel<Item, Data>,
              InnerAdapter : BaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

    override var adapterList: MutableList<Pair<Model, InnerAdapter>> = ArrayList()
    override var onInitAdapterListener: OnInitNestedAdapterListener<Data>? = null

    @InternalAdaptersApi
    override val delegate: INestedModelListManager<Item, Model, Data, InnerAdapter> by adapterConfig.listManager()

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