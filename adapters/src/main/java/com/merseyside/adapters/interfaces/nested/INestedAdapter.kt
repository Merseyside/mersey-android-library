@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.nested

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.interfaces.sorted.ISortedAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.extensions.remove

interface INestedAdapter<Parent, Model, InnerData, InnerAdapter> : ISortedAdapter<Parent, Model>,
    AdapterNestedListActions<Parent, Model, InnerData, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    var adapterList: MutableList<Pair<Model, InnerAdapter>>

    fun initNestedAdapter(model: Model): InnerAdapter
    fun getNestedView(binding: ViewDataBinding): RecyclerView?

    fun getAdapterByItem(item: Parent): InnerAdapter? {
        val model = getModelByItem(item)
        return model?.let {
            getAdapterIfExists(it)
        }
    }

    override suspend fun remove(item: Parent): Model? {
        removeAdapterByItem(item)
        return super.remove(item)
    }

    fun removeAdapterByItem(item: Parent): Boolean {
        return getModelByItem(item)?.let { model ->
            removeAdapterByModel(model)
        } ?: false
    }

    private fun putAdapter(model: Model, adapter: InnerAdapter) {
        adapterList.add(model to adapter)
    }

    private fun getAdapterIfExists(model: Model): InnerAdapter? {
        return adapterList.find { it.first.areItemsTheSame(model.item) }?.second
    }

    private fun getFilterableAdapters(): List<Filterable<InnerData, *>> {
        return adapterList
            .map { it.second }
            .filterIsInstance<Filterable<InnerData, *>>()
    }

    private fun removeAdapterByModel(model: Model): Boolean {
        return adapterList.remove { (adaptersModel, _) ->
            adaptersModel == model
        }
    }

    /* Models list actions */

    override fun getNestedAdapterByModel(model: Model): InnerAdapter {
        return getAdapterIfExists(model) ?: initNestedAdapter(model).also { adapter ->
            putAdapter(model, adapter)
        }
    }
}