package com.merseyside.adapters.base

import com.merseyside.adapters.model.BaseSelectableAdapterViewModel

abstract class BaseSelectableAdapter<M: Any, T: BaseSelectableAdapterViewModel<M>>(
    selectableMode: SelectableMode = SelectableMode.SINGLE,
    var isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE,
    isSelectEnabled: Boolean = true
) : BaseSortedAdapter<M, T>() {

    interface OnItemSelectedListener<M> {
        fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean)
    }

    interface OnSelectEnabledListener {
        fun onEnabled(isEnabled: Boolean)
    }

    enum class SelectableMode { SINGLE, MULTIPLE }

    var selectableMode: SelectableMode = selectableMode
        set(value) {
            if (field != value) {
                field = value

                if (value == SelectableMode.SINGLE) {
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

    var isSelectEnabled: Boolean = isSelectEnabled
        set(value) {
            if (value != field) {
                field = value

                onSelectEnableListener?.onEnabled(value)

                if (modelList.isNotEmpty()) {
                    modelList.forEach { model ->
                        model.setSelectEnabled(value)
                    }
                }
            }
        }

    private val listeners: MutableList<OnItemSelectedListener<M>> = ArrayList()
    private var onSelectEnableListener: OnSelectEnabledListener? = null

    private var selectedList: MutableList<T> = ArrayList()

    override fun initItemViewModel(obj: M): T {
        return super.initItemViewModel(obj).apply {
            setSelectEnabled(isSelectEnabled)
        }
    }

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

    fun setOnSelectEnableListener(listener: OnSelectEnabledListener) {
        this.onSelectEnableListener = listener
    }

    override fun add(obj: M) {
        val isNoData = isEmpty()
        val model = initItemViewModel(obj)

        add(model)

        addItemToGroup(model)

        if (isNoData) {
            if (isSelectEnabled && findSelectedItems().isEmpty()) {
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
            if (isSelectEnabled && findSelectedItems().isEmpty()) {
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
        item.setOnItemClickListener(object :
            OnItemClickListener<M> {
            override fun onItemClicked(obj: M) {
                if (isSelectEnabled) {
                    setItemSelected(item, true)
                }
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

    fun clearSelections() {
        selectedList.forEach { model ->
            model.setSelected(false)
        }

        selectedList.clear()
    }

    fun getSelectedItemsCount(): Int {
        return selectedList.size
    }

    override fun clear() {
        super.clear()

        clearSelections()
    }
}