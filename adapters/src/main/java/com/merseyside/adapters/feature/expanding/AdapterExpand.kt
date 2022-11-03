@file:Suppress("UNCHECKED_CAST")

package com.merseyside.adapters.feature.expanding

import com.merseyside.adapters.callback.HasOnItemExpandedListener
import com.merseyside.adapters.callback.OnItemExpandedListener
import com.merseyside.adapters.config.contract.OnBindItemListener
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.feature.expanding.ExpandableMode.MULTIPLE
import com.merseyside.adapters.feature.expanding.ExpandableMode.SINGLE
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.modelList.ModelListCallback
import com.merseyside.merseyLib.kotlin.logger.ILogger
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class AdapterExpand<Parent, Model>(
    private val variableId: Int,
    private val modelList: ModelList<Parent, Model>,
    expandableMode: ExpandableMode = MULTIPLE,
    isExpandEnabled: Boolean
) : HasOnItemExpandedListener<Parent>, ModelListCallback<Model>, OnBindItemListener<Parent, Model>,
    ILogger where Model : VM<Parent> {

    override val expandedListeners: MutableList<OnItemExpandedListener<Parent>> by lazy {
        ArrayList()
    }

    private val expandedList: MutableList<ExpandableItem> by lazy { ArrayList() }

    var expandableMode: ExpandableMode = expandableMode
        set(value) {
            if (field != value) {
                field = value

                if (value == SINGLE) {
                    if (expandedList.size > 1) {
                        (1 until expandedList.size).forEach { index ->
                            updateItemWithState(expandedList[index], newState = false)
                        }

                        expandedList.removeAll(expandedList.subList(1, expandedList.lastIndex))
                    }
                }
            }
        }

    private var isExpandEnabled: Boolean = isExpandEnabled
        set(value) {
            if (field != value) {
                field = value

                modelList.mapNotNull { it.asExpandable() }
                    .forEach { it.expandState.globalExpandable.value = value }
            }
        }

    private val onExpandCallback: OnExpandCallback = object : OnExpandCallback {
        override fun onExpand(item: ExpandableItem) {
            changeItemExpandedState(item)
        }

        override fun onExpand(item: ExpandableItem, expanded: Boolean) {
            if (expanded != item.isExpanded()) {
                onExpand(item)
            }
        }
    }


    init {
        modelList.addModelListCallback(this)
    }

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        holder.bind(variableId, onExpandCallback)
    }

    override fun onInserted(models: List<Model>, position: Int, count: Int) {
        val newExpandedItems = models.filterIsInstance<ExpandableItem>()
            .filter { it.isExpanded() }

        expandedList.addAll(newExpandedItems)
        notifyItemsExpanded(newExpandedItems, false)
    }

    override fun onRemoved(models: List<Model>, position: Int, count: Int) {
        val expandableItems = models.mapNotNull { it.asExpandable() }
        expandedList.removeAll(expandableItems)
    }

    override fun onChanged(
        model: Model,
        position: Int,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {}

    override fun onMoved(fromPosition: Int, toPosition: Int) {}

    fun changeItemExpandedState(item: ExpandableItem, isExpandedByUser: Boolean = true) {
        with(item) {
            if (canItemBeExpanded(item)) {
                when (expandableMode) {
                    SINGLE -> {
                        if (!isExpanded()) {
                            val expandedItem = expandedList.firstOrNull()
                            if (expandedItem != null) {
                                updateItemWithState(expandedItem, isExpandedByUser = false)
                            }
                        }

                        updateItemWithState(item, isExpandedByUser = isExpandedByUser)
                    }

                    MULTIPLE -> {
                        updateItemWithState(item, isExpandedByUser = isExpandedByUser)
                    }
                }
            }
        }
    }

    private fun updateItemWithState(
        item: ExpandableItem,
        newState: Boolean = !item.isExpanded(),
        isExpandedByUser: Boolean = false
    ): Boolean {
        return if (item.isExpanded() xor newState) {
            item.expandState.expdaned = newState
            if (newState) {
                expandedList.add(item)
            } else {
                expandedList.remove(item)
            }

            notifyItemExpanded(item, isExpandedByUser)
            true
        } else false
    }

    fun canItemBeExpanded(item: ExpandableItem): Boolean {
        return isExpandEnabled && item.expandState.expandable
    }

    private fun notifyItemsExpanded(items: List<ExpandableItem>, isExpandedByUser: Boolean) {
        items.forEach { notifyItemExpanded(it, isExpandedByUser) }
    }

    private fun notifyItemExpanded(item: ExpandableItem, isExpandedByUser: Boolean) {
        notifyOnExpanded(item.asModel().item, item.isExpanded(), isExpandedByUser)
    }

    @ExperimentalContracts
    @Contract
    internal fun Model?.isExpandable(): Boolean {
        contract {
            returns(true) implies (this@isExpandable != null && this@isExpandable is ExpandableItem)
        }

        return this != null && this is ExpandableItem
    }

    internal fun Model?.asExpandable(): ExpandableItem? {
        return if (this != null) {
            this as? ExpandableItem
        } else null
    }

    internal fun Model?.requireExpandable(): ExpandableItem {
        return if (this != null) {
            (this as? ExpandableItem) ?: throw IllegalArgumentException("Selectable item required!")
        } else throw NullPointerException()
    }

    private fun ExpandableItem.asModel(): Model {
        return this as Model
    }

    override val tag: String = "AdapterExpand"

}