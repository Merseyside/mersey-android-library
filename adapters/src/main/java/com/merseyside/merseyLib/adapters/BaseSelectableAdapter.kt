package com.merseyside.merseyLib.adapters

import com.merseyside.merseyLib.model.BaseSelectableAdapterViewModel

abstract class BaseSelectableAdapter<M: Any, T: BaseSelectableAdapterViewModel<M>>(
    selectableMode: SelectableMode = SelectableMode.SINGLE,
    var isAllowToCancelSelection: Boolean = false
) : BaseSortedAdapter<M, T>() {

    interface OnItemSelectedListener<M> {
        fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean)
    }

    enum class SelectableMode { SINGLE, MULTIPLE }

    var selectableMode: SelectableMode = selectableMode
        set(value) {
            if (field != value) {
                field = value

                if (value == SelectableMode.SINGLE) {
                    if (selectedList.isNotEmpty()) {
                        if (selectedList.size > 1) {
                            (1 until selectedList.size).forEach { index ->
                                selectedList[index].setSelected(
                                    isSelected = false,
                                    isNotifyItem = true
                                )
                            }

                            selectedList = mutableListOf(selectedList.first())
                        }
                    }
                }
            }
        }

    private val listeners: MutableList<OnItemSelectedListener<M>> = ArrayList()

    private var selectedList: MutableList<T> = ArrayList()

    fun setOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        listeners.add(listener)

        selectedList.forEach { item ->
            listener.onSelected(
                isSelected = true,
                isSelectedByUser = false,
                item = item.getItem()
            )
        }
    }

    fun removeOnItemClickListener(listener: OnItemSelectedListener<M>) {
        listeners.remove(listener)
    }

    override fun add(obj: M) {
        val isNoData = isEmpty()
        val model = initItemViewModel(obj)

        add(model)

        addItemToGroup(model)

        if (isNoData) {
            if (findSelectedItems().isEmpty()) {
                selectFirstSelectableItem()
            }
        }
    }

    override fun add(list: List<M>) {
        val isNoData = isEmpty()

        val models = itemsToModels(list)
        addModels(models)

        addItemsToGroup(models)

        if (isNoData) {
            if (findSelectedItems().isEmpty()) {
                selectFirstSelectableItem()
            }
        }
    }

    private fun selectFirstSelectableItem() {
        if (!isAllowToCancelSelection) {

            getAllModels().forEach { item ->
                setItemSelected(item)
                return
            }
        }
    }

    private fun addItemsToGroup(list: List<T>) {
        list.forEach {
            addItemToGroup(it)
        }
    }

    private fun addItemToGroup(item: T) {
        item.setOnItemClickListener(object : OnItemClickListener<M> {
            override fun onItemClicked(obj: M) {
                setItemSelected(item, true)
            }
        })
    }

    fun selectItem(item: M) {
        val found = find(item)
        setItemSelected(found!!)
    }

    @Throws(IndexOutOfBoundsException::class)
    fun selectItem(position: Int) {
        val item = getModelByPosition(position)
        setItemSelected(item)
    }

    fun selectItems(items: List<M>) {
        if (selectableMode == SelectableMode.MULTIPLE) {
            items.forEach { selectItem(it) }
        } else {
            throw IllegalStateException("Selectable mode is Single")
        }
    }

    fun getSelectedItem(): M? {
        return if (selectedList.isNotEmpty()) {
            selectedList.first().getItem()
        } else {
            null
        }
    }

    fun getSelectedItems(): List<M> {
        return selectedList.map { it.obj }
    }

    private fun isCanItemBeSelected(item: T?): Boolean {
        return (item != null && item.isSelectable())
    }

    private fun setItemSelected(item: T?, isSelectedByUser: Boolean = false) {
        if (item != null && isCanItemBeSelected(item)) {
            if (!item.isSelected()) {
                if (selectableMode == SelectableMode.SINGLE) {
                    if (selectedList.isEmpty() || selectedList.first().areItemsNotTheSame(item.obj)) {
                        if (selectedList.isNotEmpty()) {
                            selectedList.first().setSelected(
                                isSelected = false,
                                isNotifyItem = true
                            )
                            selectedList.clear()
                        }

                        selectedList.add(item)
                    }
                } else {
                    selectedList.add(item)
                }

                item.setSelected(isSelected = true, isNotifyItem = true)
                notifyItemSelected(item, isSelectedByUser)
            } else if (isAllowToCancelSelection) {
                item.setSelected(isSelected = false, isNotifyItem = true)
                selectedList.remove(item)

                notifyItemSelected(item, isSelectedByUser)
            }
        }
    }

    private fun notifyItemSelected(item: T, isSelectedByUser: Boolean) {
        listeners.forEach { listener ->
            listener.onSelected(item.getItem(), item.isSelected(), isSelectedByUser)
        }
    }

    private fun findSelectedItems(): List<T> {
        return if (selectedList.isEmpty()) {
            modelList.filter { model ->
                 isItemSelected(model)
            }.also {
                selectedList = it.toMutableList()
            }
        } else {
            selectedList
        }
    }

    private fun isItemSelected(model: T): Boolean {
        return if (isCanItemBeSelected(model)) {
            model.isSelected()
        } else {
            false
        }
    }

    override fun clear() {
        super.clear()

        selectedList.clear()
    }

}