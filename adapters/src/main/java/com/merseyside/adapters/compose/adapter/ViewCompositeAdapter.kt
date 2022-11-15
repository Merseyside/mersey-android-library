package com.merseyside.adapters.compose.adapter

import com.merseyside.adapters.compose.delegate.ViewDelegatesManager
import com.merseyside.adapters.compose.model.ViewAdapterViewModel
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.init.initAdapter
import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.model.VM

open class ViewCompositeAdapter<Parent, Model>(
    adapterConfig: AdapterConfig<Parent, Model>,
    override val delegatesManager: ViewDelegatesManager<Parent, Model> = ViewDelegatesManager()
) : CompositeAdapter<Parent, Model>(adapterConfig, delegatesManager)
        where Parent : SCV, Model : VM<Parent> {

    companion object {
        operator fun <Parent : SCV, Model> invoke(
            delegatesManager: ViewDelegatesManager<Parent, Model>,
            configure: AdapterConfig<Parent, Model>.() -> Unit
        ): ViewCompositeAdapter<Parent, Model>
                where Model : VM<Parent> {
            return initAdapter(::ViewCompositeAdapter, delegatesManager, configure)
        }
    }
}

typealias SimpleViewCompositeAdapter = ViewCompositeAdapter<SCV, ViewAdapterViewModel>