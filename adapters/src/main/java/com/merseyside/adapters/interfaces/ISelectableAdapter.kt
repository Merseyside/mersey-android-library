@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces

import com.merseyside.adapters.base.SelectableAdapter
import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.callback.OnSelectEnabledListener
import com.merseyside.adapters.ext.getAll
import com.merseyside.adapters.model.SelectableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.logger.Logger

interface ISelectableAdapter<Parent, Model: SelectableAdapterParentViewModel<out Parent, Parent>>
    : ISortedAdapter<Parent, Model>, HasOnItemSelectedListener<Parent> {

    var selectableMode: SelectableAdapter.SelectableMode
    var isSelectEnabled: Boolean
    var isAllowToCancelSelection: Boolean

    var isGroupAdapter: Boolean

    var onSelectEnableListener: OnSelectEnabledListener?
    var selectedList: MutableList<Model>

    var selectFirstOnAdd: Boolean

    override fun addOnItemSelectedListener(listener: OnItemSelectedListener<Parent>) {
        super.addOnItemSelectedListener(listener)

        selectedList.forEach { model ->
            listener.onSelected(
                item = model.item,
                isSelected = true,
                isSelectedByUser = false
            )
        }
    }

    fun removeOnItemClickListener(listener: OnItemSelectedListener<Parent>) {
        selectedListeners.remove(listener)
    }

    override fun add(items: List<Parent>): List<Model> {
        val isNoData = isEmpty()

        val addedModels = super.add(items)

        addItemsToGroup(addedModels)

        if (isNoData) {
            if (isSelectEnabled && findSelectedItems().isEmpty()) {
                selectFirstSelectableItem()
            }
        }

        return addedModels
    }

    fun selectFirstSelectableItem(force: Boolean = false) {
        if (force || (!isAllowToCancelSelection && !isGroupAdapter) || selectFirstOnAdd) {
            sortedList.getAll().forEach { item ->
                if (setItemSelected(item)) return
            }
        }
    }

    private fun addItemsToGroup(list: List<Model>) {
        list.forEach {
            addItemToGroup(it)
        }
    }

    private fun addItemToGroup(item: Model) {
        item.setOnItemClickListener(object :
            OnItemClickListener<Parent> {
            override fun onItemClicked(obj: Parent) {
                if (isSelectEnabled) {
                    setItemSelected(item, true)
                }
            }
        })
    }

    fun selectItem(item: Parent) {
        find(item)?.let {
            setItemSelected(it)
        } ?: Logger.logErr("Item for selection not found!")
    }

    @Throws(IndexOutOfBoundsException::class)
    fun selectItem(position: Int) {
        val item = getModelByPosition(position)
        setItemSelected(item)
    }

    fun selectItems(items: List<Parent>) {
        if (selectableMode == SelectableAdapter.SelectableMode.MULTIPLE) {
            items.forEach { selectItem(it) }
        } else {
            throw IllegalStateException("Selectable mode is Single")
        }
    }

    fun getSelectedItem(): Parent? {
        return if (selectedList.isNotEmpty()) {
            selectedList.first().item
        } else {
            null
        }
    }

    fun getSelectedItemPosition(): Int {
        return getSelectedItem()?.let {
            getPositionOfItem(it)
        } ?: SelectableAdapter.NO_SELECTIONS
    }

    fun getSelectedItems(): List<Parent> {
        return selectedList.map { it.item }
    }

    private fun canItemBeSelected(item: Model?): Boolean {
        return (item != null && item.isSelectable())
    }

    fun setItemSelected(model: Model?, isSelectedByUser: Boolean = false): Boolean {
        return if (model != null && canItemBeSelected(model)) {
            if (!model.isSelected()) {
                if (selectableMode == SelectableAdapter.SelectableMode.SINGLE) {
                    if (selectedList.isEmpty() || !selectedList.first().areItemsTheSame(model.item)) {
                        if (selectedList.isNotEmpty()) {
                            with(selectedList.first()) {
                                setSelected(false)
                                notifyItemSelected(this, isSelectedByUser = false)
                            }
                            selectedList.clear()
                        }

                        selectedList.add(model)
                    }
                } else {
                    selectedList.add(model)
                }

                model.setSelected(true)
                notifyItemSelected(model, isSelectedByUser)
            } else if (isAllowToCancelSelection) {
                model.setSelected(false)
                selectedList.remove(model)

                notifyItemSelected(model, isSelectedByUser)
            }
            true
        } else {
            false
        }
    }

    private fun notifyItemSelected(item: Model, isSelectedByUser: Boolean) {
        selectedListeners.forEach { listener ->
            listener.onSelected(item.item, item.isSelected(), isSelectedByUser)
        }
    }

    private fun notifyItemsSelected(items: List<Model>, isSelectedByUser: Boolean) {
        items.forEach { notifyItemSelected(it, isSelectedByUser) }
    }

    private fun findSelectedItems(): List<Model> {
        return if (selectedList.isEmpty()) {
            getModels().filter { model ->
                isItemSelected(model)
            }.also {
                selectedList = it.toMutableList()
            }
        } else {
            selectedList
        }
    }

    private fun isItemSelected(model: Model): Boolean {
        return if (canItemBeSelected(model)) {
            model.isSelected()
        } else {
            false
        }
    }

    fun clearSelections() {
        selectedList.forEach { model ->
            model.setSelected(false)
        }
        notifyItemsSelected(selectedList, isSelectedByUser = false)
        selectedList.clear()
    }

    fun getSelectedItemsCount(): Int {
        return selectedList.size
    }

    override fun clear() {
        super.clear()
        clearSelections()
    }

    override fun remove(item: Parent): Boolean {
        return try {
            super.remove(item)
        } finally {
            getModelByItem(item)?.let {
                removeSelected(listOf(it))
            }
        }
    }

    override fun removeModels(list: List<Model>): Boolean {
        return try {
            super.removeModels(list)
        } finally {
            removeSelected(list)
        }
    }

    private fun removeSelected(list: List<Model>) {
        if (list.isNotEmpty()) {
            val selectedItemsGoingToRemove =
                selectedList.intersect(list.toSet()).toList()

            if (selectedItemsGoingToRemove.isNotEmpty()) {
                selectedItemsGoingToRemove.forEach { model ->
                    model.setSelected(false, notifyItem = false)
                }

                notifyItemsSelected(selectedItemsGoingToRemove, isSelectedByUser = true)
                selectedList.removeAll(selectedItemsGoingToRemove)
                notifyItemsRemoved(selectedItemsGoingToRemove)

                if (!isGroupAdapter && !isAllowToCancelSelection) {
                    selectFirstSelectableItem()
                }
            }
        }
    }

    private fun notifyItemsRemoved(items: List<Model>) {
        selectedListeners.forEach { listener ->
            listener.onSelectedRemoved(this, items.map { it.item })
        }
    }

    fun selectFirstOnAdd() {
        selectFirstOnAdd = true
    }

    override fun notifyAdapterRemoved() {
        with(selectedList) {
            val clone = selectedList.toList()

            if (isNotEmpty()) {
                clearSelections()
            }

            clear()
            notifyItemsRemoved(clone)
        }
    }
}