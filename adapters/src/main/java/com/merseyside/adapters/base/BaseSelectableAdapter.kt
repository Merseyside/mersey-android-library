package com.merseyside.adapters.base

import com.merseyside.adapters.model.BaseSelectableAdapterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.jvm.Throws

abstract class BaseSelectableAdapter<M: Any, T: BaseSelectableAdapterViewModel<M>>(
    selectableMode: SelectableMode = SelectableMode.SINGLE,
    var isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE,
    isSelectEnabled: Boolean = true,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseSortedAdapter<M, T>(scope), HasOnItemSelectedListener<M> {

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

    override val selectedListeners: MutableList<OnItemSelectedListener<M>> = ArrayList()
    private var onSelectEnableListener: OnSelectEnabledListener? = null

    private var selectedList: MutableList<T> = ArrayList()

    override fun initItemViewModel(obj: M): T {
        return super.initItemViewModel(obj).apply {
            setSelectEnabled(isSelectEnabled)
        }
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        super.setOnItemSelectedListener(listener)

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

    private fun canItemBeSelected(item: T?): Boolean {
        return (item != null && item.isSelectable())
    }

    private fun setItemSelected(item: T?, isSelectedByUser: Boolean = false) {
        if (item != null && canItemBeSelected(item)) {
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
}