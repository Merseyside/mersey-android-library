package com.merseyside.adapters.interfaces.delegate

import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface IDelegateAdapter<Item : Parent, Parent, Model> : HasOnItemClickListener<Item>
        where Model : AdapterParentViewModel<Item, Parent> {

    @InternalAdaptersApi
    val onClick: (Item) -> Unit

    fun getBindingVariable(): Int



    fun onModelUpdated(model: Model) {}
}