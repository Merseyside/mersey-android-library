package com.merseyside.adapters.feature.composable.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.feature.composable.delegate.ViewDelegatesManager
import com.merseyside.adapters.feature.composable.model.ViewAdapterViewModel
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class ViewCompositeAdapter<Parent, Model>(
    adapterConfig: AdapterConfig<Parent, Model> = AdapterConfig(),
    override val delegatesManager: ViewDelegatesManager<Parent, Model> = ViewDelegatesManager()
) : CompositeAdapter<Parent, Model>(adapterConfig, delegatesManager)
        where Parent: SCV,
              Model : AdapterParentViewModel<out Parent, Parent> {

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