package com.merseyside.adapters.feature.selecting.group

import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.config.contract.HasWorkManager
import com.merseyside.adapters.feature.selecting.AdapterSelect
import com.merseyside.adapters.interfaces.selectable.ISelectableAdapter
import com.merseyside.adapters.interfaces.selectable.SelectableMode
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue
import com.merseyside.merseyLib.kotlin.extensions.isNotZero

class SelectableAdapterGroup<Item>(
    var selectableMode: SelectableMode,
    var isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE
) : HasOnItemSelectedListener<Item>, HasWorkManager {

    override lateinit var workManager: CoroutineQueue<Any, Unit>

    private val groupSelectedListener = object : OnItemSelectedListener<Item> {
        override fun onSelected(item: Item, isSelected: Boolean, isSelectedByUser: Boolean) {
            when (selectableMode) {
                SelectableMode.SINGLE -> {
                    if (isSelected) {
                        val adaptersWithSelectedItems = getAdaptersWithSelectedItems()

                        if (adaptersWithSelectedItems.isNotEmpty()) {
                            adaptersWithSelectedItems
                                .find { it.getSelectedItem() != item }
                                ?.clear()
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
            doAsync { selectMostAppropriateItem(adapterList) }
        }
    }

    override val selectedListeners: MutableList<OnItemSelectedListener<Item>> =
        mutableListOf(groupSelectedListener)
    private val adapters: MutableList<AdapterSelect<Item, *>> = mutableListOf()

    fun addAsync(adapter: AdapterSelect<Item, *>, onComplete: (Unit) -> Unit = {}) {
        doAsync(onComplete) { add(adapter) }
    }

    fun add(adapter: AdapterSelect<Item, *>) {
        adapter.isGroupAdapter = true
        adapters.add(adapter)
        selectedListeners.forEach { listener ->
            adapter.addOnItemSelectedListener(listener)
        }

        if (!isAllowToCancelSelection && adapters.size == 1) {
            adapter.selectFirstOnAdd = true
        }
    }

    suspend fun removeAsync(adapter: AdapterSelect<Item, *>, onComplete: (Unit) -> Unit = {}) {
        doAsync(onComplete) { remove(adapter) }
    }

    suspend fun remove(adapter: AdapterSelect<Item, *>) {
        adapters.remove(adapter)
        if (adapter.size.isNotZero() &&
            !isAllowToCancelSelection && selectableMode == SelectableMode.SINGLE
        ) {
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
            it.removeOnItemSelectedListener(listener)
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
                adapter.selectFirstSelectableItem()
                return
            }
        }
    }

    private fun getAdaptersWithSelectedItems(): List<AdapterSelect<Item, *>> {
        return adapters.filter { it.size.isNotZero() }
    }

    private fun selectMostAppropriateItem(adapter: ISelectableAdapter<Item, *>) {
        if (!isAllowToCancelSelection) {
            if (adapter.getItemCount().isNotZero()) {
                adapter.selectFirstSelectableItem(force = true)
            } else {
                selectFirstItem()
            }
        }
    }
}