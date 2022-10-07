package com.merseyside.adapters.feature.composable.delegate

import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.utils.ext.values

@Suppress("UNCHECKED_CAST")
class ViewDelegatesManager<Parent : SCV, Model>(
    delegates: List<DelegateAdapter<out Parent, Parent, Model>> = emptyList()
) : DelegatesManager<DelegateAdapter<out Parent, Parent, Model>, Parent, Model>(delegates)
        where Model : AdapterParentViewModel<out Parent, Parent> {

    override fun getResponsibleDelegate(item: Parent): DelegateAdapter<out Parent, Parent, Model>? {
        val adapter = super.getResponsibleDelegate(item)
        return adapter
    }

    fun getAllDelegates(): List<ViewDelegateAdapter<out Parent, *, *>> {
        return delegates.values() as List<ViewDelegateAdapter<out Parent, *, *>>
    }
}