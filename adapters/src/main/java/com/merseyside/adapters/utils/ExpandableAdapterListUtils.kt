@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.utils

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.base.SortedAdapter
import com.merseyside.adapters.base.UpdateRequest
import com.merseyside.adapters.model.ExpandableAdapterParentViewModel
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import com.merseyside.merseyLib.kotlin.extensions.remove

interface ExpandableAdapterListUtils<Parent, Model : ExpandableAdapterParentViewModel<out Parent, Parent, Data>,
        Data, InnerAdapter : BaseAdapter<Data, *>>
    : SelectableAdapterListUtils<Parent, Model> {
    var adapterList: MutableList<Pair<Model, InnerAdapter>>

    fun initExpandableList(model: Model): InnerAdapter
    fun getExpandableView(binding: ViewDataBinding): RecyclerView?

    @InternalAdaptersApi
    override fun addModels(list: List<Model>) {
        super.addModels(list)
        addModelsToAdapters(list)
    }

    private fun addModelsToAdapters(list: List<Model>) {
        list.forEach { model ->
            val adapter = getExpandableAdapter(model)
            val data = model.getExpandableData()

            addExpandableItems(adapter, data)
        }
    }

    fun getAdapterByItem(item: Parent): InnerAdapter? {
        val model = getModelByItem(item)
        return model?.let {
            getAdapterIfExists(it)
        }
    }

    fun getExpandableAdapters(): List<InnerAdapter> {
        return adapterList.map { it.second }
    }

    private fun putAdapter(model: Model, adapter: InnerAdapter) {
        adapterList.add(model to adapter)
    }

    private fun getAdapterIfExists(model: Model): InnerAdapter? {
        return adapterList.find { it.first.areItemsTheSame(model.item) }?.second
    }


    override fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        if (updateRequest.isDeleteOld) {
            adapterList = adapterList.filter { adapter ->
                updateRequest.list.find { adapter.first.areItemsTheSame(it) } != null
            }.toMutableList()
        }

        return try {
            super.update(updateRequest)
        } finally {
            updateExpandableAdapters()
        }
    }

    override fun update(item: Parent): Boolean {
        val updated = super.update(item)

        if (updated) {
            val model = getModelByItem(item)
            if (model != null) {
                val adapter = getExpandableAdapter(model)
                model.getExpandableData().isNotNullAndEmpty {
                    adapter.update(UpdateRequest(this))
                }
            }
        }

        return updated
    }

    override fun setFilter(query: String): Int {
        return try {
            super.setFilter(query)
        } finally {
            if (query.isEmpty()) {
                getAllModels().forEach { model ->
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
                val hasDatas = try {
                    it.setFilter(filterPattern) > 0
                } catch (e: NotImplementedError) {
                    false
                }

                val passedFilter = filter(model, filterPattern)
                hasDatas || passedFilter
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

    override fun remove(items: List<Parent>): Boolean {
        removeAdaptersByItems(items)
        return super.remove(items)
    }

    override fun remove(item: Parent): Boolean {
        removeAdaptersByItems(listOf(item))
        return super.remove(item)
    }

    private fun removeAdaptersByItems(list: List<Parent>) {
        val adapters = list.mapNotNull { getAdapterByItem(it) }
        adapterList.remove { adapters.find { second -> it == second } != null }

        adapters.forEach { it.notifyAdapterRemoved() }
        onAdaptersRemoved(adapters)
    }

    @InternalAdaptersApi
    fun getExpandableAdapter(model: Model): InnerAdapter {
        return getAdapterIfExists(model) ?: initExpandableList(model).also { adapter ->
            putAdapter(model, adapter)
        }
    }

    private fun updateExpandableAdapters() {
        getAllModels().forEach { model ->
            val adapter = getExpandableAdapter(model)
            addExpandableItems(adapter, model.getExpandableData())
        }
    }

    fun getExpandableAdapterUpdateRequest(data: List<Data>?): UpdateRequest<Data>? {
        if (data == null) return null
        return UpdateRequest.Builder(data)
            .isAddNew(true)
            .isDeleteOld(true)
            .build()
    }

    private fun addExpandableItems(adapter: InnerAdapter, list: List<Data>?) {
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

    private fun getFilterableAdapters(): List<SortedAdapter<Data, *>> {
        return adapterList
            .map { it.second }
            .filterIsInstance<SortedAdapter<Data, *>>()
    }

    fun onAdaptersRemoved(adapters: List<InnerAdapter>) {}

    private fun add(adapter: InnerAdapter, list: List<Data>) {
        with(adapter) {
            if (addJob?.isActive == true) {
                (this as? SortedAdapterListUtils<Data, *>) ?: throw Exception()
                addAsync(list)
                return
            }
            adapter.add(list)
        }
    }

    private fun update(adapter: InnerAdapter, list: List<Data>?) {
        with(adapter) {
            getExpandableAdapterUpdateRequest(list)?.let { request ->
                if (updateJob?.isActive == true) {
                    (this as? SortedAdapterListUtils<Data, *>) ?: throw Exception()
                    updateAsync(request)
                    return
                }
                adapter.update(request)
            }
        }
    }
}