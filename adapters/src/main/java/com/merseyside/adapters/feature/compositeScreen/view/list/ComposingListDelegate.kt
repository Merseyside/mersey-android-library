package com.merseyside.adapters.feature.compositeScreen.view.list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.BR
import com.merseyside.adapters.R
import com.merseyside.adapters.databinding.ViewComposingListBinding
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.extensions.onClick
import com.merseyside.adapters.feature.compositeScreen.adapter.ViewCompositeAdapter
import com.merseyside.adapters.feature.compositeScreen.delegate.NestedViewDelegateAdapter
import com.merseyside.adapters.utils.InternalAdaptersApi

open class ComposingListDelegate :
    NestedViewDelegateAdapter<ComposingList, ComposingListStyle, ComposingListViewModel>() {

    override fun getNestedView(binding: ViewDataBinding, model: ComposingListViewModel): RecyclerView? {
        return (binding as ViewComposingListBinding).list.also { recyclerView ->
            model.item.decorator?.let { recyclerView.addItemDecoration(it) }
        }
    }

    override fun getLayoutIdForItem(viewType: Int) = R.layout.view_composing_list
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: ComposingList) = ComposingListViewModel(item)

    @InternalAdaptersApi
    override fun initNestedAdapter(
        model: ComposingListViewModel,
        delegatesManager: DelegatesManager<*, *, *>
    ): ViewCompositeAdapter {
        return super.initNestedAdapter(model, delegatesManager).also { adapter ->
            with(adapter) {
                onClick { view -> model.item.notifyOnClick(view) }
            }
        }
    }

}