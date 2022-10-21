package com.merseyside.adapters.feature.composable.view.list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.BR
import com.merseyside.adapters.R
import com.merseyside.adapters.databinding.ViewComposingListBinding
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.extensions.onClick
import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.feature.composable.adapter.ViewCompositeAdapter
import com.merseyside.adapters.feature.composable.delegate.NestedViewDelegateAdapter
import com.merseyside.adapters.feature.composable.view.list.simple.ComposingList
import com.merseyside.adapters.feature.composable.view.list.simple.ComposingListStyle
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class BaseComposingListDelegate<View, Model, InnerParent, InnerModel, InnerAdapter>
    : NestedViewDelegateAdapter<View, ComposingListStyle, Model, InnerParent, InnerModel, InnerAdapter>()
        where
              View : ComposingList,
              Model : NestedAdapterParentViewModel<View, SCV, out InnerParent>,
              InnerParent: SCV,
              InnerModel : AdapterParentViewModel<out InnerParent, InnerParent>,
              InnerAdapter : ViewCompositeAdapter<InnerParent, out InnerModel> {

    override fun getLayoutIdForItem(viewType: Int) = R.layout.view_composing_list

    override fun getNestedView(binding: ViewDataBinding, model: Model): RecyclerView? {
        return (binding as ViewComposingListBinding).list.also { recyclerView ->
            model.item.decorator?.let { recyclerView.addItemDecoration(it) }
        }
    }

    override fun getBindingVariable() = BR.model

    @InternalAdaptersApi
    override fun initNestedAdapter(
        model: Model,
        delegatesManager: DelegatesManager<*, *, *>
    ): InnerAdapter {
        return super.initNestedAdapter(model, delegatesManager).also { adapter ->
            with(adapter) {
                onClick { view -> model.item.notifyOnClick(view) }
            }
        }
    }
}