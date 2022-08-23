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

    override fun addModel(model: Model) {
        super.addModel(model)
        model.onExpandedCallback = internalExpandedCallback as
                ((ExpandableAdapterParentViewModel<out Parent, Parent, InnerData>) -> Unit)
    }
}