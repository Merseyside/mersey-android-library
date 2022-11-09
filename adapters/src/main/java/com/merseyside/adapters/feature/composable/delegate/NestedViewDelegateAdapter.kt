package com.merseyside.adapters.feature.composable.delegate

import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.feature.composable.SCV
import com.merseyside.adapters.feature.composable.StyleableComposingView
import com.merseyside.adapters.feature.composable.adapter.ViewCompositeAdapter
import com.merseyside.adapters.feature.style.ComposingStyle
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.delegate.INestedDelegateAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class NestedViewDelegateAdapter<View : StyleableComposingView<Style>, Style : ComposingStyle, Model>
    : PrioritizedViewDelegateAdapter<View, Style, Model>(),
    INestedDelegateAdapter<View, SCV, Model, SCV, ViewCompositeAdapter>
        where Model : NestedAdapterParentViewModel<View, SCV, SCV> {

    override var adapterList: MutableList<Pair<Model, ViewCompositeAdapter>> = ArrayList()

    override lateinit var delegatesManagerProvider: () -> DelegatesManager<*, *, *>

    override fun initNestedAdapter(
        model: Model,
        delegatesManager: DelegatesManager<*, *, *>
    ): ViewCompositeAdapter {

        val delegates = getApplicableDelegates(delegatesManager)
        val innerDelegateManager = ViewDelegatesManager(delegates)
        return ViewCompositeAdapter(delegatesManager = innerDelegateManager)
    }

    @Suppress("UNCHECKED_CAST")
    fun getApplicableDelegates(
        delegatesManager: DelegatesManager<*, *, *>
    ): List<ViewDelegateAdapter<out SCV, *, AdapterParentViewModel<out SCV, SCV>>> {
        delegatesManager as ViewDelegatesManager
        return delegatesManager.getAllDelegates()
    }

    @InternalAdaptersApi
    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        super.onBindViewHolder(holder, model, position)
        bindNestedAdapter(holder, model, position)
    }
}