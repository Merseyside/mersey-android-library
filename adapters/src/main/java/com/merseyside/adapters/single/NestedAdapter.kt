package com.merseyside.adapters.single

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.feature.filter.delegate.FilterNestedListChangeDelegate
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.listDelegates.NestedListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterNestedListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterViewModel
import com.merseyside.adapters.utils.getFilter
import com.merseyside.adapters.utils.isFilterable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class NestedAdapter<Item, Model, Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : SortedAdapter<Item, Model>(scope),
    INestedAdapter<Item, Model, Data, InnerAdapter>
        where Model : NestedAdapterViewModel<Item, Data>,
              InnerAdapter : BaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

    override var adapterList: MutableList<Pair<Model, InnerAdapter>> = ArrayList()

    override val defaultDelegate: NestedListChangeDelegate<Item, Model, Data, InnerAdapter> by lazy {
        NestedListChangeDelegate(this)
    }

    override val filterDelegate: FilterNestedListChangeDelegate<Item, Model, Data, InnerAdapter> by lazy {
        FilterNestedListChangeDelegate(defaultDelegate, getFilter())
    }

    override val delegate: AdapterNestedListChangeDelegate<Item, Model, Data, InnerAdapter> by lazy {
        if (isFilterable()) filterDelegate else defaultDelegate
    }

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