package com.merseyside.adapters.interfaces.simple

import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.list.AdapterListChangeDelegate

interface SimpleAdapterListActions<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    AdapterListActions<Parent, Model>,
    AdapterListChangeDelegate.OnListChangedCallback<Parent, Model> {

    @InternalAdaptersApi
    fun addModel(index: Int, model: Model)
}