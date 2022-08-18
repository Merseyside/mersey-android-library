@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.interfaces.nested

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.interfaces.sorted.ISortedAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import com.merseyside.merseyLib.kotlin.extensions.remove

interface INestedAdapter<Parent, Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : IBaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>>>
    : ISortedAdapter<Parent, Model> {

    var adapterList: MutableList<Pair<Model, InnerAdapter>>

    fun initNestedAdapter(model: Model): InnerAdapter
    fun getNestedView(binding: ViewDataBinding): RecyclerView?

    override fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        if (updateRequest.isDeleteOld) {
            adapterList = adapterList.filter { adapter ->
                updateRequest.list.find { adapter.first.areItemsTheSame(it) } != null
            }.toMutableList()
        }

        return super.update(updateRequest)
    }


    override fun remove(items: List<Parent>) {
        removeAdaptersByItems(items)
        super.remove(items)
    }

    override fun remove(item: Parent): Boolean {
        removeAdaptersByItems(listOf(item))
        return super.remove(item)
    }

    fun getAdapterByItem(item: Parent): InnerAdapter? {
        val model = getModelByItem(item)
        return model?.let {
            getAdapterIfExists(it)
        }
    }

    private fun removeAdaptersByItems(list: List<Parent>) {
        val adapters = list.mapNotNull { getAdapterByItem(it) }
        adapterList.remove { adapters.find { second -> it == second } != null }

        adapters.forEach { it.notifyAdapterRemoved() }
        onAdaptersRemoved(adapters)
    }

    private fun putAdapter(model: Model, adapter: InnerAdapter) {
        adapterList.add(model to adapter)
    }

    private fun getAdapterIfExists(model: Model): InnerAdapter? {
        return adapterList.find { it.first.areItemsTheSame(model.item) }?.second
    }

    fun getNestedAdapter(model: Model): InnerAdapter {
        return getAdapterIfExists(model) ?: initNestedAdapter(model).also { adapter ->
            putAdapter(model, adapter)
        }
    }

    private fun updateNestedAdapters() {
        models.forEach { model ->
            val adapter = getNestedAdapter(model)
            addNestedItems(adapter, model.getNestedData())
        }
    }

    private fun addNestedItems(adapter: InnerAdapter, list: List<InnerData>?) {
        with(adapter) {
            if (isEmpty()) {
                if (list != null) {
                    add(this, list)
                }
            } else {
                update(this, list)
            }
        }
    }

    private fun getFilterableAdapters(): List<Filterable<InnerData, *>> {
        return adapterList
            .map { it.second }
            .filterIsInstance<Filterable<InnerData, *>>()
    }

    fun onAdaptersRemoved(adapters: List<InnerAdapter>) {}

    private fun add(adapter: InnerAdapter, list: List<InnerData>) {
        with(adapter) {
            if (addJob?.isActive == true) {
                addAsync(list)
                return
            }
            add(list)
        }
    }

    private fun update(adapter: InnerAdapter, list: List<InnerData>?) {
        with(adapter) {
            update(list ?: emptyList())
        }
    }

    /* Models list actions */

    @InternalAdaptersApi
    override fun addModels(models: List<Model>) {
        super.addModels(models)
        addModelsToAdapters(models)
    }

    private fun addModelsToAdapters(list: List<Model>) {
        list.forEach { model ->
            val adapter = getNestedAdapter(model)
            val data = model.getNestedData()

            addNestedItems(adapter, data)
        }
    }

    override fun updateModel(model: Model, item: Parent): Boolean {
        val isUpdated = super.updateModel(model, item)

        if (isUpdated) {
            val adapter = getNestedAdapter(model)
            model.getNestedData().isNotNullAndEmpty {
                adapter.update(this)
            }
        }

        return isUpdated
    }
}