package com.merseyside.adapters.utils

import com.merseyside.adapters.base.SelectableAdapter
import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.interfaces.selectable.ISelectableAdapter
import com.merseyside.adapters.interfaces.selectable.SelectableMode
import com.merseyside.merseyLib.kotlin.extensions.isNotZero

class SelectableAdapterGroup<Item>(
    var selectableMode: SelectableMode = SelectableMode.SINGLE,
    var isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE
) : HasOnItemSelectedListener<Item> {

    private val groupSelectedListener = object: OnItemSelectedListener<Item> {
        override fun onSelected(item: Item, isSelected: Boolean, isSelectedByUser: Boolean) {
            when (selectableMode) {
                SelectableMode.SINGLE -> {
                    if (isSelected) {
                        val adaptersWithSelectedItems = getAdaptersWithSelectedItems()

                        if (adaptersWithSelectedItems.isNotEmpty()) {
                            val oldAdapter =
                                adaptersWithSelectedItems.find { it.getSelectedItem() != item }
                            oldAdapter?.run {
                                clearSelections()
                            }
                        }
                    }
                }

                SelectableMode.MULTIPLE -> {

                }
            }
        }

        override fun onSelectedRemoved(
            adapterList: ISelectableAdapter<Item, *>,
            items: List<Item>
        ) {
            selectMostAppropriateItem(adapterList)
        }
    }

    override val selectedListeners: MutableList<OnItemSelectedListener<Item>> = mutableListOf(groupSelectedListener)
    private val adapters: MutableList<SelectableAdapter<Item, *>> = mutableListOf()

    fun add(adapter: SelectableAdapter<Item, *>) {
        adapter.groupAdapter = true
        adapters.add(adapter)
        selectedListeners.forEach { listener ->
            adapter.addOnItemSelectedListener(listener)
        }

        if (!isAllowToCancelSelection && adapters.size == 1) {
            adapter.selectFirstOnAdd()
        }
    }

    fun remove(adapter: SelectableAdapter<Item, *>) {
        adapters.remove(adapter)
        if (adapter.getSelectedItemsCount().isNotZero() &&
            !isAllowToCancelSelection && selectableMode == SelectableMode.SINGLE) {
            selectFirstItem()
        }
    }

    override fun addOnItemSelectedListener(listener: OnItemSelectedListener<Item>) {
        super.addOnItemSelectedListener(listener)
        adapters.forEach {
            it.addOnItemSelectedListener(listener)
        }
    }

    override fun removeOnItemSelectedListener(listener: OnItemSelectedListener<Item>) {
        super.removeOnItemSelectedListener(listener)
        adapters.forEach {
            it.removeOnItemClickListener(listener)
        }
    }

    fun getSelectedItems(): List<Item> {
        return adapters.flatMap {
            it.getSelectedItems()
        }
    }

    private fun selectFirstItem() {
        adapters.forEach { adapter ->
            if (adapter.isSelectEnabled) {
                adapter.selectFirstSelectableItem(force = true)
                return
            }
        }
    }

    private fun getAdaptersWithSelectedItems(): List<SelectableAdapter<Item, *>> {
        return adapters.filter { it.getSelectedItemsCount().isNotZero() }
    }

    private fun selectMostAppropriateItem(adapter: ISelectableAdapter<Item, *>) {
        if (!isAllowToCancelSelection) {
            if (adapter.getItemsCount().isNotZero()) {
                adapter.selectFirstSelectableItem(force = true)
            } else {
                selectFirstItem()
            }
        }
    }
}