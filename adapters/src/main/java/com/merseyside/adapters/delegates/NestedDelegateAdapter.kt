package com.merseyside.adapters.delegates

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.delegate.INestedDelegateAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class NestedDelegateAdapter<Item : Parent, Parent, Model, Data, InnerAdapter>
    : DelegateAdapter<Item, Parent, Model>(),
    INestedDelegateAdapter<Item, Parent, Model, Data, InnerAdapter>
        where Model : NestedAdapterParentViewModel<Item, Parent, Data>,
              InnerAdapter : BaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

    override val adapterList: MutableList<Pair<Model, InnerAdapter>> = ArrayList()

    @InternalAdaptersApi
    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        super.onBindViewHolder(holder, model, position)
        bindNestedAdapter(holder, model, position)
    }
}