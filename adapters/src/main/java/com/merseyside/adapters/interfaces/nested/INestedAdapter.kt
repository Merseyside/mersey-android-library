@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.nested

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.listManager.INestedIModelListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.extensions.remove

interface INestedAdapter<Parent, Model, InnerData, InnerAdapter> : IBaseAdapter<Parent, Model>,
    NestedAdapterActions<Parent, Model, InnerData, InnerAdapter>, HasNestedAdapterListener<InnerData>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    var adapterList: MutableList<Pair<Model, InnerAdapter>>

    override val delegate: INestedIModelListManager<Parent, Model, InnerData, InnerAdapter>

    fun initNestedAdapter(model: Model): InnerAdapter
    fun getNestedView(binding: ViewDataBinding): RecyclerView?

    private fun internalInitInnerAdapter(model: Model): InnerAdapter {
        return initNestedAdapter(model).also { innerAdapter ->
            onInitAdapterListener?.onInitNestedAdapter(innerAdapter)
        }
    }

    @OptIn(InternalAdaptersApi::class)
    suspend fun getNestedAdapterByItem(item: Parent): InnerAdapter? {
        val model = getModelByItem(item)
        return model?.let {
            getAdapterIfExists(it)
        }
    }

    private fun putAdapter(model: Model, adapter: InnerAdapter) {
        adapterList.add(model to adapter)
    }

    private fun getAdapterIfExists(model: Model): InnerAdapter? {
        return adapterList.find { it.first.areItemsTheSame(model.item) }?.second
    }

    /* Models list actions */

    override fun getNestedAdapterByModel(model: Model): InnerAdapter {
        return getAdapterIfExists(model) ?: internalInitInnerAdapter(model).also { adapter ->
            putAdapter(model, adapter)
        }
    }

    override fun removeNestedAdapterByModel(model: Model): Boolean {
        return adapterList.remove { (adaptersModel, _) ->
            adaptersModel == model
        }
    }

    @InternalAdaptersApi
    override suspend fun removeAll() {
        super.removeAll()
        adapterList.clear()
    }
}