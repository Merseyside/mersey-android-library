package com.merseyside.adapters.compose.delegate

import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.utils.ext.values
import com.merseyside.adapters.model.VM

@Suppress("UNCHECKED_CAST")
class ViewDelegatesManager<Parent : SCV, Model>(
    delegates: List<DelegateAdapter<out Parent, Parent, Model>> = emptyList()
) : DelegatesManager<DelegateAdapter<out Parent, Parent, Model>, Parent, Model>(delegates)
        where Model : VM<Parent> {

    override fun getResponsibleDelegate(item: Parent): DelegateAdapter<out Parent, Parent, Model>? {
        val adapter = super.getResponsibleDelegate(item)
        return adapter
    }

    fun getAllDelegates(): List<ViewDelegateAdapter<out Parent, *, *>> {
        return delegates.values() as List<ViewDelegateAdapter<out Parent, *, *>>
    }
}