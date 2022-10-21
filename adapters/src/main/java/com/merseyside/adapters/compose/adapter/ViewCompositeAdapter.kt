package com.merseyside.adapters.compose.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.delegate.ViewDelegatesManager
import com.merseyside.adapters.compose.model.ViewAdapterViewModel
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.VM

open class ViewCompositeAdapter<Parent, Model>(
    adapterConfig: AdapterConfig<Parent, Model> = AdapterConfig(),
    override val delegatesManager: ViewDelegatesManager<Parent, Model> = ViewDelegatesManager()
) : CompositeAdapter<Parent, Model>(adapterConfig, delegatesManager)
        where Parent: SCV,
              Model : VM<Parent> {

    override fun onBindViewHolder(
        holder: TypedBindingHolder<Model>,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        onModelUpdated(holder.model)
    }

    internal open fun onModelUpdated(model: Model) {
        val delegate = delegatesManager.getResponsibleDelegate(model.item)
        delegate?.onModelUpdated(model)
    }
}

typealias SimpleViewCompositeAdapter = ViewCompositeAdapter<SCV, ViewAdapterViewModel>