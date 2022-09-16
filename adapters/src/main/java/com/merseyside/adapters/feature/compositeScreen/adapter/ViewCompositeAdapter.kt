package com.merseyside.adapters.feature.compositeScreen.adapter

import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.feature.compositeScreen.SCV
import com.merseyside.adapters.feature.compositeScreen.ComposingView as CV
import com.merseyside.adapters.feature.compositeScreen.delegate.ViewDelegatesManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class ViewCompositeAdapter(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    override val delegatesManager: ViewDelegatesManager = ViewDelegatesManager()
) : CompositeAdapter<SCV, AdapterParentViewModel<out SCV, SCV>>(
    scope,
    delegatesManager
) {

    override fun onBindViewHolder(
        holder: TypedBindingHolder<AdapterParentViewModel<out SCV, SCV>>,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        onModelUpdated(holder.model)
    }

    internal open fun onModelUpdated(model: AdapterParentViewModel<out SCV, SCV>) {
        val delegate = delegatesManager.getResponsibleDelegate(model.item)
        delegate?.onModelUpdated(model)
    }
}