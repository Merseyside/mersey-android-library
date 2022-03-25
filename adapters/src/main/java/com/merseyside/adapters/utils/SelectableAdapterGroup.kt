package com.merseyside.adapters.utils

import com.merseyside.adapters.base.SelectableAdapter
import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.merseyLib.kotlin.extensions.isNotZero

class SelectableAdapterGroup<M>(
    var selectableMode: SelectableAdapter.SelectableMode = SelectableAdapter.SelectableMode.SINGLE,
    var isAllowToCancelSelection: Boolean = selectableMode == SelectableAdapter.SelectableMode.MULTIPLE
) : HasOnItemSelectedListener<M> {

    private val groupSelectedListener = object: OnItemSelectedListener<M> {
        override fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean) {
            when (selectableMode) {
                SelectableAdapter.SelectableMode.SINGLE -> {
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

                SelectableAdapter.SelectableMode.MULTIPLE -> {

                }
            }
        }

        override fun onSelectedRemoved(
            adapterList: SelectableAdapterListUtils<M, *>,
            items: List<M>
        ) {
            selectMostAppropriateItem(adapterList)
        }
    }

    override val selectedListeners: MutableList<OnItemSelectedListener<M>> = mutableListOf(groupSelectedListener)
    private val adapters: MutableList<SelectableAdapter<M, *>> = mutableListOf()

    fun add(adapter: SelectableAdapter<M, *>) {
        adapter.groupAdapter = true
        adapters.add(adapter)
        selectedListeners.forEach { listener ->
            adapter.addOnItemSelectedListener(listener)
        }

        if (!isAllowToCancelSelection && adapters.size == 1) {
            adapter.selectFirstOnAdd()
        }
    }

    fun remove(adapter: SelectableAdapter<M, *>) {
        adapters.remove(adapter)
        if (adapter.getSelectedItemsCount().isNotZero() &&
            !isAllowToCancelSelection && selectableMode == SelectableAdapter.SelectableMode.SINGLE) {
            selectFirstItem()
        }
    }

    override fun addOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        super.addOnItemSelectedListener(listener)
        adapters.forEach {
            it.addOnItemSelectedListener(listener)
        }
    }

    override fun removeOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        super.removeOnItemSelectedListener(listener)
        adapters.forEach {
            it.removeOnItemClickListener(listener)
        }
    }

    fun getSelectedItems(): List<M> {
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

    private fun getAdaptersWithSelectedItems(): List<SelectableAdapter<M, *>> {
        return adapters.filter { it.getSelectedItemsCount().isNotZero() }
    }

    private fun selectMostAppropriateItem(adapter: SelectableAdapterListUtils<M, *>) {
        if (!isAllowToCancelSelection) {
            if (adapter.getAllItemCount().isNotZero()) {
                adapter.selectFirstSelectableItem(force = true)
            } else {
                selectFirstItem()
            }
        }
    }
}