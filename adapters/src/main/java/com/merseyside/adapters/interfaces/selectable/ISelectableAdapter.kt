@file:OptIn(InternalAdaptersApi::class)
@file:Suppress("UNCHECKED_CAST")

package com.merseyside.adapters.interfaces.selectable

import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.callback.OnSelectEnabledListener
import com.merseyside.adapters.interfaces.sorted.ISortedAdapter
import com.merseyside.adapters.model.SelectableAdapterParentViewModel
import com.merseyside.adapters.single.SelectableAdapter
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.logger.Logger

interface ISelectableAdapter<Parent, Model>
    : ISortedAdapter<Parent, Model>, HasOnItemSelectedListener<Parent>
        where Model : SelectableAdapterParentViewModel<out Parent, Parent> {

    var selectableMode: SelectableMode
    var isSelectEnabled: Boolean
    var isAllowToCancelSelection: Boolean

    var isGroupAdapter: Boolean

    var onSelectEnableListener: OnSelectEnabledListener?
    var selectedList: MutableList<Model>

    var selectFirstOnAdd: Boolean

    val internalOnSelect: (parent: Parent) -> Unit

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

    override suspend fun onModelCreated(model: Model) {
        super.onModelCreated(model)
        model.isSelectable = isSelectEnabled
        model.selectEvent.observe(internalOnSelect)
    }

    override suspend fun addModels(models: List<Model>) {
        val isNoData = isEmpty()
        super.addModels(models)
        if (isNoData) {
            if (isSelectEnabled && findSelectedItems().isEmpty()) {
                selectFirstSelectableItem()
            }
        }
    }

    suspend fun selectFirstSelectableItem(force: Boolean = false) {
        if (force || (!isAllowToCancelSelection && !isGroupAdapter) || selectFirstOnAdd) {
            models.forEach { item ->
                if (setModelSelected(item)) return
            }
        }
    }

    fun selectItemAsync(item: Parent, onComplete: (Unit) -> Unit = {}) {
        doAsync(onComplete) { selectItem(item) }
    }

    suspend fun selectItem(item: Parent) {
        find(item)?.let { model ->
            setModelSelected(model)
        } ?: Logger.logErr("Item for selection not found!")
    }

    @Throws(IndexOutOfBoundsException::class)
    suspend fun selectItem(position: Int) {
        val item = getModelByPosition(position)
        setModelSelected(item)
    }

    fun selectItemsAsync(items: List<Parent>, onComplete: (Unit) -> Unit = {}) {
        doAsync(onComplete) { selectItems(items) }
    }

    suspend fun selectItems(items: List<Parent>) {
        if (selectableMode == SelectableMode.MULTIPLE) {
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

    suspend fun getSelectedItemPosition(): Int {
        return getSelectedItem()?.let {
            getPositionOfItem(it)
        } ?: SelectableAdapter.NO_SELECTIONS
    }

    fun getSelectedItems(): List<Parent> {
        return selectedList.map { it.item }
    }

    private fun canModelBeSelected(item: Model?): Boolean {
        return item != null && item.isSelectable
    }

    suspend fun setModelSelected(model: Model?, isSelectedByUser: Boolean = false): Boolean {
        return if (model != null && canModelBeSelected(model)) {
            if (!model.isSelected) {
                if (selectableMode == SelectableMode.SINGLE) {
                    if (selectedList.isEmpty() ||
                        !selectedList.first().areItemsTheSame(model.item)
                    ) {
                        if (selectedList.isNotEmpty()) {
                            with(selectedList.first()) {
                                isSelected = false
                                notifyAllSelectedListeners(
                                    model.item,
                                    model.isSelected,
                                    isSelectedByUser = false
                                )
                            }
                            selectedList.clear()
                        }

                        selectedList.add(model)
                    }
                } else {
                    selectedList.add(model)
                }

                model.isSelected = true
                notifyAllSelectedListeners(model.item, model.isSelected, isSelectedByUser)
            } else if (isAllowToCancelSelection) {
                model.isSelected = false
                selectedList.remove(model)

                notifyAllSelectedListeners(model.item, model.isSelected, isSelectedByUser)
            }
            true
        } else {
            false
        }
    }

    private fun notifyModelsSelected(models: List<Model>, isSelectedByUser: Boolean) {
        models.forEach { model ->
            notifyAllSelectedListeners(
                model.item,
                model.isSelected,
                isSelectedByUser
            )
        }
    }

    private fun findSelectedItems(): List<Model> {
        return if (selectedList.isEmpty()) {
            models.filter { model ->
                isItemSelected(model)
            }.also {
                selectedList = it.toMutableList()
            }
        } else {
            selectedList
        }
    }

    private fun isItemSelected(model: Model): Boolean {
        return if (canModelBeSelected(model)) {
            model.isSelected
        } else {
            false
        }
    }

    fun clearSelections() {
        selectedList.forEach { model ->
            model.isSelected = false
        }
        notifyModelsSelected(selectedList, isSelectedByUser = false)
        selectedList.clear()
    }

    fun getSelectedItemsCount(): Int {
        return selectedList.size
    }

    override suspend fun clear() {
        super.clear()
        clearSelections()
    }

    override suspend fun remove(item: Parent): Model? {
        return try {
            super.remove(item)
        } finally {
            getModelByItem(item)?.let {
                removeSelected(listOf(it))
            }
        }
    }

    private suspend fun removeSelected(list: List<Model>) {
        if (list.isNotEmpty()) {
            val selectedItemsGoingToRemove =
                selectedList.intersect(list.toSet()).toList()

            if (selectedItemsGoingToRemove.isNotEmpty()) {
                selectedItemsGoingToRemove.forEach { model ->
                    model.isSelected = false
                }

                notifyModelsSelected(selectedItemsGoingToRemove, isSelectedByUser = true)
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