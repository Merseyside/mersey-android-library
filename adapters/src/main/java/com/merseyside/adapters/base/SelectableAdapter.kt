package com.merseyside.adapters.base

import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.callback.OnSelectEnabledListener
import com.merseyside.adapters.model.SelectableAdapterViewModel
import com.merseyside.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class SelectableAdapter<M: Any, T: SelectableAdapterViewModel<M>>(
    selectableMode: SelectableMode = SelectableMode.SINGLE,
    var isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE,
    isSelectEnabled: Boolean = true,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : SortedAdapter<M, T>(scope), HasOnItemSelectedListener<M> {

    enum class SelectableMode { SINGLE, MULTIPLE }
    internal var groupAdapter: Boolean = false
    private var selectFirstOnAdd: Boolean = false
        get() {
            return try {
                field
            } finally {
                field = false
            }
        }

    var selectableMode: SelectableMode = selectableMode
        set(value) {
            if (field != value) {
                field = value

                if (value == SelectableMode.SINGLE) {
                    if (selectedList.size > 1) {
                        (1 until selectedList.size).forEach { index ->
                            selectedList[index].setSelected(false)
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

    override val selectedListeners: MutableList<OnItemSelectedListener<M>> = ArrayList()
    private var onSelectEnableListener: OnSelectEnabledListener? = null

    private var selectedList: MutableList<T> = ArrayList()

    override fun initItemViewModel(obj: M): T {
        return super.initItemViewModel(obj).apply {
            setSelectEnabled(isSelectEnabled)
        }
    }

    override fun addOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        super.addOnItemSelectedListener(listener)

        selectedList.forEach { item ->
            listener.onSelected(
                item = item.getItem(),
                isSelected = true,
                isSelectedByUser = false
            )
        }
    }

    fun removeOnItemClickListener(listener: OnItemSelectedListener<M>) {
        selectedListeners.remove(listener)
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
        if ((!isAllowToCancelSelection && !groupAdapter) || selectFirstOnAdd) {
            sortedList.getAll().forEach { item ->
                if (setItemSelected(item)) return
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
        find(item)?.let {
            setItemSelected(it)
        } ?: Logger.logErr("Item for selection not found!")
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

    fun getSelectedItemPosition(): Int {
        return getSelectedItem()?.let {
            getPositionOfItem(it)
        } ?: NO_SELECTIONS
    }

    fun getSelectedItems(): List<M> {
        return selectedList.map { it.obj }
    }

    private fun canItemBeSelected(item: T?): Boolean {
        return (item != null && item.isSelectable())
    }

    private fun setItemSelected(item: T?, isSelectedByUser: Boolean = false): Boolean {
        return if (item != null && canItemBeSelected(item)) {
            if (!item.isSelected()) {
                if (selectableMode == SelectableMode.SINGLE) {
                    if (selectedList.isEmpty() || selectedList.first().areItemsNotTheSame(item.obj)) {
                        if (selectedList.isNotEmpty()) {
                            selectedList.first().setSelected(false)
                            selectedList.clear()
                        }

                        selectedList.add(item)
                    }
                } else {
                    selectedList.add(item)
                }

                item.setSelected(true)
                notifyItemSelected(item, isSelectedByUser)
            } else if (isAllowToCancelSelection) {
                item.setSelected(false)
                selectedList.remove(item)

                notifyItemSelected(item, isSelectedByUser)
            }

            recyclerView.invalidateItemDecorations()
            true
        } else {
            false
        }
    }

    private fun notifyItemSelected(item: T, isSelectedByUser: Boolean) {
        selectedListeners.forEach { listener ->
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

        selectedList.clear()
    }

    fun getSelectedItemsCount(): Int {
        return selectedList.size
    }

    override fun clear() {
        super.clear()
        clearSelections()
    }

    override fun removeListeners() {
        super.removeListeners()
        selectedListeners.clear()
    }

    internal fun selectFirstOnAdd() {
        selectFirstOnAdd = true
    }

    companion object {
        const val NO_SELECTIONS = -1
    }
}