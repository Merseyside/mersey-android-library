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

    @OptIn(InternalAdaptersApi::class)
    suspend fun getAdapterByItem(item: Parent): InnerAdapter? {
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

    private fun getFilterableAdapters(): List<Filterable<InnerData, *>> {
        return adapterList
            .map { it.second }
            .filterIsInstance<Filterable<InnerData, *>>()
    }

    /* Models list actions */

    override fun getNestedAdapterByModel(model: Model): InnerAdapter {
        return getAdapterIfExists(model) ?: initNestedAdapter(model).also { adapter ->
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