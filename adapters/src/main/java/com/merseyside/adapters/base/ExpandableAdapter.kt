package com.merseyside.adapters.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.model.ExpandableAdapterViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import com.merseyside.merseyLib.kotlin.extensions.remove
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ExpandableAdapter<M : Any, T : ExpandableAdapterViewModel<M, SubItem>,
        SubItem : Any, InnerAdapter : BaseAdapter<SubItem, *>>(
    selectableMode: SelectableMode = SelectableMode.MULTIPLE,
    isAllowToCancelSelection: Boolean = true,
    scope: CoroutineScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob()
    )
) : SelectableAdapter<M, T>(selectableMode, isAllowToCancelSelection, scope = scope) {

    private var adapterList: MutableList<Pair<T, InnerAdapter>> = ArrayList()
    private var updateRequest: UpdateRequest<M>? = null

    /*Inner adapters add out of sync, so we have to be sure when can work with them */
    var onAdapterInitializedCallback: (InnerAdapter) -> Unit = {}

    override fun onBindViewHolder(holder: TypedBindingHolder<T>, position: Int) {
        super.onBindViewHolder(holder, position)
        val model = getModelByPosition(position)
        val data = model.getExpandableData()

        val recyclerView: RecyclerView? = getExpandableView(holder.binding)
        recyclerView?.apply {

            val newAdapter = getAdapterIfExists(model) == null
            val adapter = getExpandableAdapter(model)

            this.adapter = adapter

            data.isNotNullAndEmpty {
                addExpandableItems(adapter, this)
            }

            if (newAdapter) {
                onAdapterInitializedCallback(adapter)
            }
        }
    }

    fun getAdapterByItem(item: M): InnerAdapter? {
        val model = getModelByObj(item)
        return model?.let {
            getAdapterIfExists(it)
        }
    }

    fun getExpandableAdapters(): List<InnerAdapter> {
        return adapterList.map { it.second }
    }

    private fun putAdapter(model: T, adapter: InnerAdapter) {
        adapterList.add(model to adapter)
    }

    private fun getAdapterIfExists(model: T): InnerAdapter? {
        return adapterList.find { it.first.areItemsTheSame(model.getItem()) }?.second
    }

    abstract fun initExpandableList(model: T): InnerAdapter

    abstract fun getExpandableView(binding: ViewDataBinding): RecyclerView?

    override fun update(updateRequest: UpdateRequest<M>): Boolean {
        this.updateRequest = updateRequest

        if (updateRequest.isDeleteOld) {
            adapterList = adapterList.filter { adapter ->
                updateRequest.list.find { adapter.first.areItemsTheSame(it) } != null
            }.toMutableList()
        }

        return try {
            super.update(updateRequest)
        } finally {
            updateExpandableAdapters()
            this.updateRequest = null
        }
    }

    override fun update(obj: M): Boolean {
        val updated = super.update(obj)

        if (updated) {
            val model = getModelByObj(obj)
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

    override fun setFilter(models: List<T>): List<T>? {
        return models.filter { model ->
            val adapter = getAdapterIfExists(model)
            adapter?.let {
                val hasSubItems = try {
                    it.setFilter(filterPattern) > 0
                } catch (e: NotImplementedError) {
                    false
                }

                val passedFilter = filter(model, filterPattern)
                hasSubItems || passedFilter
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

    override fun applyFilters(models: List<T>): List<T>? {
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

    fun removeOnAdapterInitializedCallback() {
        onAdapterInitializedCallback = {}
    }

    override fun remove(list: List<M>) {
        removeAdaptersByItems(list)
        super.remove(list)
    }

    override fun remove(obj: M) {
        removeAdaptersByItems(listOf(obj))
        super.remove(obj)
    }

    private fun removeAdaptersByItems(list: List<M>) {
        val adapters = list.mapNotNull { getAdapterByItem(it) }
        adapterList.remove { adapters.find { second -> it == second } != null }

        adapters.forEach { it.notifyAdapterRemoved() }
        onAdaptersRemoved(adapters)
    }

    private fun getExpandableAdapter(model: T): InnerAdapter {
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

    open fun getExpandableAdapterUpdateRequest(data: List<SubItem>?): UpdateRequest<SubItem>? {
        if (data == null) return null
        return UpdateRequest.Builder(data)
            .isAddNew(true)
            .isDeleteOld(true)
            .build()
    }

    private fun addExpandableItems(adapter: InnerAdapter, list: List<SubItem>?) {
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

    private fun getFilterableAdapters(): List<SortedAdapter<SubItem, *>> {
        return adapterList
            .map { it.second }
            .filterIsInstance<SortedAdapter<SubItem, *>>()
    }

    open fun onAdaptersRemoved(adapters: List<InnerAdapter>) {}

    private fun add(adapter: InnerAdapter, list: List<SubItem>) {
        with(adapter) {
            if (addJob?.isActive == true) {
                if (this is SortedAdapter<*, *>) {
                    this as SortedAdapter<SubItem, *>
                    addAsync(list)
                    return
                }
            }
            adapter.add(list)
        }
    }

    private fun update(adapter: InnerAdapter, list: List<SubItem>?) {
        with(adapter) {
            getExpandableAdapterUpdateRequest(list)?.let { request ->
                adapter.update(request)

                if (updateJob?.isActive == true) {
                    if (this is SortedAdapter<*, *>) {
                        this as SortedAdapter<SubItem, *>
                        updateAsync(request)
                        return
                    }
                }
                adapter.update(request)
            }
        }
    }
}