package com.merseyside.adapters.utils

import com.merseyside.adapters.base.SelectableAdapter
import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.utils.ext.isNotZero

class SelectableAdapterGroup<M : Any>(
    var selectableMode: SelectableAdapter.SelectableMode = SelectableAdapter.SelectableMode.SINGLE,
    var isAllowToCancelSelection: Boolean = selectableMode == SelectableAdapter.SelectableMode.MULTIPLE
) : HasOnItemSelectedListener<M> {

    private val groupSelectedListener = object: OnItemSelectedListener<M> {
        override fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean) {
            when (selectableMode) {
                SelectableAdapter.SelectableMode.SINGLE -> {
                    val adaptersWithSelectedItems = getAdaptersWithSelectedItems()

                    if (adaptersWithSelectedItems.isNotEmpty()) {
                        val oldAdapter = adaptersWithSelectedItems.find { it.getSelectedItem() != item }
                        oldAdapter?.clearSelections()
                    }
                }

                SelectableAdapter.SelectableMode.MULTIPLE -> {

                }
            }

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

    private fun getAdaptersWithSelectedItems(): List<SelectableAdapter<M, *>> {
        return adapters.filter { it.getSelectedItemsCount().isNotZero() }
    }
}