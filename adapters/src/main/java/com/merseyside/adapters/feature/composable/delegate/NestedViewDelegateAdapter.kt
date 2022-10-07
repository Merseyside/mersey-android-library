package com.merseyside.adapters.feature.composable.delegate

import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.feature.composable.view.base.StyleableComposingView
import com.merseyside.adapters.feature.composable.adapter.ViewCompositeAdapter
import com.merseyside.adapters.feature.composable.model.ViewAdapterViewModel
import com.merseyside.adapters.feature.style.ComposingStyle
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.delegate.INestedDelegateAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class NestedViewDelegateAdapter<View, Style, Model, InnerParent, InnerModel, InnerAdapter> :
    PrioritizedViewDelegateAdapter<View, Style, Model>(),
    INestedDelegateAdapter<View, SCV, Model, InnerParent, InnerAdapter>
        where View : StyleableComposingView<Style>,
              InnerParent : SCV,
              Style : ComposingStyle,
              Model : NestedAdapterParentViewModel<View, SCV, out InnerParent>,
              InnerModel : AdapterParentViewModel<out InnerParent, InnerParent>,
              InnerAdapter : ViewCompositeAdapter<InnerParent, out InnerModel> {

    override var adapterList: MutableList<Pair<Model, InnerAdapter>> = ArrayList()

    override lateinit var delegatesManagerProvider: () -> DelegatesManager<*, *, *>

    abstract fun createCompositeAdapter(delegateManager: ViewDelegatesManager<InnerParent, InnerModel>): InnerAdapter

    override fun initNestedAdapter(
        model: Model,
        delegatesManager: DelegatesManager<*, *, *>
    ): InnerAdapter {

        val innerDelegateManager = delegatesManager as ViewDelegatesManager<InnerParent, InnerModel>
        return createCompositeAdapter(innerDelegateManager)
    }

    @InternalAdaptersApi
    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        super.onBindViewHolder(holder, model, position)
        bindNestedAdapter(holder, model, position)
    }
}