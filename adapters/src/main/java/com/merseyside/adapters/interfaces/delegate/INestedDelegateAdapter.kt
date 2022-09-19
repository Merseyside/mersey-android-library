package com.merseyside.adapters.interfaces.delegate

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.extensions.remove

interface INestedDelegateAdapter<Item : Parent, Parent, Model, Data, InnerAdapter>
    : IPrioritizedDelegateAdapter<Item, Parent, Model>
        where Model : NestedAdapterParentViewModel<Item, Parent, Data>,
              InnerAdapter : BaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

    var delegatesManagerProvider: () -> DelegatesManager<*, *, *>

    val adapterList: MutableList<Pair<Model, InnerAdapter>>

    fun initNestedAdapter(model: Model, delegatesManager: DelegatesManager<*, *, *>): InnerAdapter

    fun getNestedView(binding: ViewDataBinding, model: Model): RecyclerView?

    private fun getNestedAdapterByModel(model: Model): InnerAdapter {
        return getAdapterIfExists(model) ?: initNestedAdapter(model, delegatesManagerProvider())
            .also { adapter ->
                putAdapter(model, adapter)
            }
    }

    fun removeNestedAdapterByModel(model: Model): Boolean {
        return adapterList.remove { (adaptersModel, _) ->
            adaptersModel == model
        }
    }


    override fun onModelUpdated(model: Model) {
        val adapter = getNestedAdapterByModel(model)
        setInnerData(adapter, model)
    }

    private fun getAdapterIfExists(model: Model): InnerAdapter? {
        return adapterList.find { it.first.areItemsTheSame(model.item) }?.second
    }

    private fun putAdapter(model: Model, adapter: InnerAdapter) {
        adapterList.add(model to adapter)
    }

    private fun setInnerData(adapter: InnerAdapter, model: Model) {
        model.getNestedData()?.let { data ->
            adapter.addOrUpdateAsync(data)
        }
    }

    @InternalAdaptersApi
    fun bindNestedAdapter(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        getNestedView(holder.binding, model)?.apply {
            val adapter = getNestedAdapterByModel(model)
            setInnerData(adapter, model)
            if (this.adapter != adapter) {
                this.adapter = adapter
            }
        }
    }
}