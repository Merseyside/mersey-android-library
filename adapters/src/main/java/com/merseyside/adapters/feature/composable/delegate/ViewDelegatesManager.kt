package com.merseyside.adapters.feature.composable.delegate

import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.feature.composable.SCV
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.utils.ext.values

@Suppress("UNCHECKED_CAST")
class ViewDelegatesManager(
    delegates: List<DelegateAdapter<out SCV, SCV, ViewAdapterViewModel>> = emptyList()
) : DelegatesManager<DelegateAdapter<out SCV, SCV, ViewAdapterViewModel>,
            SCV, ViewAdapterViewModel>(delegates) {

    override fun getResponsibleDelegate(
        item: SCV
    ): ViewDelegate? {
        val adapter = super.getResponsibleDelegate(item)
        return adapter as? ViewDelegate
    }

    fun getAllDelegates(): List<ViewDelegate> {
        return delegates.values() as List<ViewDelegate>
    }
}

internal typealias ViewDelegate = ViewDelegateAdapter<out SCV, *, ViewAdapterViewModel>
typealias ViewAdapterViewModel = AdapterParentViewModel<out SCV, SCV>