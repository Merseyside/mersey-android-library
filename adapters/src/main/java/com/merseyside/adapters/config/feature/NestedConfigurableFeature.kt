package com.merseyside.adapters.config.feature

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.NestedAdapterConfig
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.model.NestedAdapterParentViewModel

abstract class NestedConfigurableFeature<Parent, Model, Data, InnerAdapter, Config> :
    ConfigurableFeature<Parent, Model, Config>()
    where Model : NestedAdapterParentViewModel<out Parent, Parent, Data>,
          InnerAdapter : BaseAdapter<Data, *> {

    @Suppress("UNCHECKED_CAST")
    final override fun install(
        adapterConfig: AdapterConfig<Parent, Model>,
        adapter: IBaseAdapter<Parent, Model>
    ) {
        super.install(adapterConfig, adapter)
        install(
            adapterConfig as NestedAdapterConfig<Parent, Model, Data, InnerAdapter>,
            adapter as INestedAdapter<Parent, Model, Data, InnerAdapter>
        )
    }

    abstract fun install(
        adapterConfig: NestedAdapterConfig<Parent, Model, Data, InnerAdapter>,
        adapter: INestedAdapter<Parent, Model, Data, InnerAdapter>
    )
}