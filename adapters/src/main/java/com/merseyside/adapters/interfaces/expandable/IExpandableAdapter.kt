@file:OptIn(InternalAdaptersApi::class)
@file:Suppress("UNCHECKED_CAST")

package com.merseyside.adapters.interfaces.expandable

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.callback.HasOnItemExpandedListener
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ExpandableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface IExpandableAdapter<Parent, Model, InnerData, InnerAdapter>
    : INestedAdapter<Parent, Model, InnerData, InnerAdapter>, HasOnItemExpandedListener<Parent>
        where Model : ExpandableAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    var expandableMode: ExpandableMode
    val internalExpandedCallback: (Model) -> Unit

    @InternalAdaptersApi
    fun getExpandedModels(): List<Model> {
        return models.filter { it.isExpanded }
    }

    fun getExpandedItems(): List<Parent> {
        return getExpandedModels().map { it.item }
    }

    fun changeModelExpandedState(model: Model, isExpandedByUser: Boolean = true) {
        val newExpandedState = !model.isExpanded
        applyExpandableMode(newExpandedState)
        model.isExpanded = newExpandedState
        notifyAllExpandedListeners(model.item, newExpandedState, isExpandedByUser)
    }

    private fun applyExpandableMode(newState: Boolean) {
        if (expandableMode == ExpandableMode.SINGLE) {
            if (newState) {
                getExpandedModels().forEach {
                    changeModelExpandedState(it, isExpandedByUser = false)
                }
            }
        }
    }

//    override fun setFilter(query: String): Int {
//        return try {
//            super.setFilter(query)
//        } finally {
//            if (query.isEmpty()) {
//                models.forEach { model ->
//                    val adapter = getAdapterIfExists(model)
//                    adapter?.setFilter(query)
//                }
//            }
//        }
//    }
//
//    override fun setFilter(models: List<Model>): List<Model>? {
//        return models.filter { model ->
//            val adapter = getAdapterIfExists(model)
//            adapter?.let {
//                val hasData = try {
//                    it.setFilter(filterPattern) > 0
//                } catch (e: NotImplementedError) {
//                    false
//                }
//
//                val passedFilter = filter(model, filterPattern)
//                hasData || passedFilter
//            } ?: false
//        }.also { isFiltered = true }
//    }
//
//    override fun addFilter(key: String, obj: Any) {
//        super.addFilter(key, obj)
//        getFilterableAdapters()
//            .forEach { subAdapter ->
//                subAdapter.addFilter(key, obj)
//            }
//    }
//
//    override fun removeFilter(key: String) {
//        super.removeFilter(key)
//        getFilterableAdapters()
//            .forEach { subAdapter ->
//                subAdapter.removeFilter(key)
//            }
//    }
//
//    @InternalAdaptersApi
//    override fun applyFilters(models: List<Model>): List<Model>? {
//        val expandableModels = super.applyFilters(models)
//        return expandableModels?.filter { model ->
//            val subAdapter = getAdapterIfExists(model)
//            if (subAdapter is SortedAdapter<*, *>) {
//                subAdapter.applyFilters() > 0
//            } else true
//        }
//    }
//
//    override fun clearFilters() {
//        super.clearFilters()
//        getFilterableAdapters().forEach { it.clearFilters() }
//    }

    override fun addModel(model: Model) {
        super.addModel(model)
        model.onExpandedCallback = internalExpandedCallback as
                ((ExpandableAdapterParentViewModel<out Parent, Parent, InnerData>) -> Unit)
    }
}