@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.SortedAdapter
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ExpandableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import com.merseyside.merseyLib.kotlin.extensions.remove

interface IExpandableAdapter<Parent, Model : ExpandableAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : IBaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>>>
    : ISelectableAdapter<Parent, Model> {
    var adapterList: MutableList<Pair<Model, InnerAdapter>>

    fun initExpandableList(model: Model): InnerAdapter
    fun getExpandableView(binding: ViewDataBinding): RecyclerView?

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


    override fun setFilter(query: String): Int {
        return try {
            super.setFilter(query)
        } finally {
            if (query.isEmpty()) {
                models.forEach { model ->
                    val adapter = getAdapterIfExists(model)
                    adapter?.setFilter(query)
                }
            }
        }
    }

    override fun setFilter(models: List<Model>): List<Model>? {
        return models.filter { model ->
            val adapter = getAdapterIfExists(model)
            adapter?.let {
                val hasData = try {
                    it.setFilter(filterPattern) > 0
                } catch (e: NotImplementedError) {
                    false
                }

                val passedFilter = filter(model, filterPattern)
                hasData || passedFilter
            } ?: false
        }.also { isFiltered = true }
    }

    override fun addFilter(key: String, obj: Any) {
        super.addFilter(key, obj)
        getFilterableAdapters()
            .forEach { subAdapter ->
                subAdapter.addFilter(key, obj)
            }
    }

    override fun removeFilter(key: String) {
        super.removeFilter(key)
        getFilterableAdapters()
            .forEach { subAdapter ->
                subAdapter.removeFilter(key)
            }
    }

    @InternalAdaptersApi
    override fun applyFilters(models: List<Model>): List<Model>? {
        val expandableModels = super.applyFilters(models)
        return expandableModels?.filter { model ->
            val subAdapter = getAdapterIfExists(model)
            if (subAdapter is SortedAdapter<*, *>) {
                subAdapter.applyFilters() > 0
            } else true
        }
    }

    override fun clearFilters() {
        super.clearFilters()
        getFilterableAdapters().forEach { it.clearFilters() }
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

    @InternalAdaptersApi
    fun getExpandableAdapter(model: Model): InnerAdapter {
        return getAdapterIfExists(model) ?: initExpandableList(model).also { adapter ->
            putAdapter(model, adapter)
        }
    }

    private fun updateExpandableAdapters() {
        models.forEach { model ->
            val adapter = getExpandableAdapter(model)
            addExpandableItems(adapter, model.getExpandableData())
        }
    }

    private fun addExpandableItems(adapter: InnerAdapter, list: List<InnerData>?) {
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

    private fun getFilterableAdapters(): List<SortedAdapter<InnerData, *>> {
        return adapterList
            .map { it.second }
            .filterIsInstance<SortedAdapter<InnerData, *>>()
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
            val adapter = getExpandableAdapter(model)
            val data = model.getExpandableData()

            addExpandableItems(adapter, data)
        }
    }

    override fun updateModel(model: Model, item: Parent): Boolean {
        val isUpdated = super.updateModel(model, item)

        if (isUpdated) {
            val adapter = getExpandableAdapter(model)
            model.getExpandableData().isNotNullAndEmpty {
                adapter.update(this)
            }
        }

        return isUpdated
    }
}