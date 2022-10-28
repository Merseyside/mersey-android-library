package com.merseyside.adapters.feature.selecting

import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.config.contract.HasWorkManager
import com.merseyside.adapters.config.contract.OnBindItemListener
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.selectable.SelectableMode
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.modelList.ModelListCallback
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue
import com.merseyside.merseyLib.kotlin.logger.log

@Suppress("UNCHECKED_CAST")
class AdapterSelect<Parent, Model>(
    private val modelList: ModelList<Parent, Model>,
    private val variableId: Int,
    selectableMode: SelectableMode,
    isSelectEnabled: Boolean,
    private var isAllowToCancelSelection: Boolean
) : HasOnItemSelectedListener<Parent>, OnBindItemListener<Parent, Model>,
    OnSelectCallback, ModelListCallback<Model>, HasWorkManager
        where Model : VM<Parent> {

    internal var isGroupAdapter: Boolean = false
    internal var selectFirstOnAdd: Boolean = false

    override val selectedListeners: MutableList<OnItemSelectedListener<Parent>> by lazy {
        ArrayList()
    }

    override lateinit var workManager: CoroutineQueue<Any, Unit>

    private val selectedList: MutableList<SelectableItem> = ArrayList()

    val size: Int
        get() = selectedList.size

    var selectableMode: SelectableMode = selectableMode
        set(value) {
            if (field != value) {
                field = value

                if (value == SelectableMode.SINGLE) {
                    if (selectedList.size > 1) {
                        (1 until selectedList.size).forEach { index ->
                            updateItemWithState(selectedList[index], newState = false)
                        }

                        selectedList.removeAll(selectedList.subList(1, selectedList.lastIndex))
                    }
                }
            }
        }

    var isSelectEnabled: Boolean = isSelectEnabled
        set(value) {
            if (value != field) {
                field = value

                modelList.forEach { model ->
                    model.asSelectable().selectState.globalSelectable.value = value
                }

                if (selectedList.isEmpty() && selectableMode == SelectableMode.SINGLE &&
                        !isAllowToCancelSelection) {
                    selectFirstItemIfNeed()
                }
            }
        }

    init {
        modelList.addModelListCallback(this)
    }


    override fun onBindViewHolder(
        holder: TypedBindingHolder<Model>,
        model: Model,
        position: Int
    ) {
        holder.bind(variableId, this as OnSelectCallback)
    }


    override fun onInserted(models: List<Model>, position: Int, count: Int) {
        if (modelList.size == count) {
            if (isSelectEnabled && selectedList.isEmpty() && selectableMode == SelectableMode.SINGLE) {
                selectFirstItemIfNeed()
            }
        }

        initNewModels(models)
    }

    private fun initNewModels(models: List<Model>) {
        val selectableItems = models.filterIsInstance<SelectableItem>()
        selectableItems.forEach { item ->
            item.selectState.globalSelectable.value = isSelectEnabled
        }
    }

    override fun onRemoved(models: List<Model>, position: Int, count: Int) {
        removeSelected(models)
    }

    override fun onChanged(
        model: Model,
        position: Int,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {}


    override fun addOnItemSelectedListener(listener: OnItemSelectedListener<Parent>) {
        super.addOnItemSelectedListener(listener)

        selectedList.forEach { item ->
            listener.onSelected(
                item = (item as Model).item,
                isSelected = true,
                isSelectedByUser = false
            )
        }
    }

    suspend fun selectItem(item: Parent) {
        val selectable = modelList.getModelByItem(item).requireSelectable()
        setItemSelected(selectable)
    }

    fun getSelectedItem(): Parent? {
        return if (selectedList.isNotEmpty()) {
            selectedList.first().asModel().item
        } else {
            null
        }
    }

    fun getSelectedItems(): List<Parent> {
        return selectedList
            .map { it.asModel() }
            .map { it.item }
    }

    private fun updateItemWithState(
        item: SelectableItem,
        newState: Boolean = !item.isSelected(),
        isSelectedByUser: Boolean = false
    ): Boolean {
        return if (item.isSelected() xor newState) {

            item.selectState.selected = newState
            if (newState) {
                selectedList.add(item)
            } else {
                selectedList.remove(item)
            }

            notifyItemSelected(item, isSelectedByUser)

            true
        } else false
    }

    fun setItemSelected(item: SelectableItem, isSelectedByUser: Boolean = false): Boolean {

        return with(item) {
            if (canItemBeSelected(item)) {
                when (selectableMode) {
                    SelectableMode.SINGLE -> {
                        if (!isSelected()) {
                            selectedList.log()
                            val selectedItem = selectedList.firstOrNull()
                            if (selectedItem != null) {
                                updateItemWithState(
                                    selectedItem,
                                    newState = false,
                                    isSelectedByUser = false
                                )
                            }

                            selectedList.clear()
                            updateItemWithState(item, isSelectedByUser = isSelectedByUser)
                        } else {
                            if (isAllowToCancelSelection) {
                                updateItemWithState(item, isSelectedByUser = isSelectedByUser)
                            } else false
                        }
                    }

                    SelectableMode.MULTIPLE -> {
                        if (isSelected()) {
                            if (isAllowToCancelSelection) {
                                updateItemWithState(item)
                            } else false
                        } else {
                            updateItemWithState(item)
                        }
                    }
                }
            } else false
        }
    }

    private fun isItemSelected(model: SelectableItem): Boolean {
        return if (canItemBeSelected(model)) {
            model.isSelected()
        } else {
            false
        }
    }

    private fun canItemBeSelected(item: SelectableItem): Boolean {
        return isSelectEnabled && item.selectState.selectable
    }

    private fun selectFirstItemIfNeed() {
        if (isSelectEnabled && !isAllowToCancelSelection) {
            selectFirstSelectableItem()
        }
    }

    fun selectFirstSelectableItem() {
        modelList.forEach { item ->
            if (setItemSelected(item.asSelectable())) return
        }
    }

    internal fun Model?.asSelectable(): SelectableItem {
        return if (this != null) {
            this as SelectableItem
        } else throw NullPointerException()
    }

    internal fun Model?.requireSelectable(): SelectableItem {
        return if (this != null) {
            (this as? SelectableItem) ?: throw IllegalArgumentException("Selectable item required!")
        } else throw NullPointerException()
    }

    private fun SelectableItem.asModel(): Model {
        return this as Model
    }

    private fun removeSelected(list: List<Model>) {
        if (list.isNotEmpty()) {
            val selectedItemsGoingToRemove: Set<SelectableItem> =
                selectedList.intersect(list.map { it as SelectableItem }.toSet())

            if (selectedItemsGoingToRemove.isNotEmpty()) {
                selectedItemsGoingToRemove.forEach { item ->
                    updateItemWithState(item, false)
                }

                selectedList.removeAll(selectedItemsGoingToRemove)
                //notifyItemsRemoved(selectedItemsGoingToRemove)

                if (!isGroupAdapter && !isAllowToCancelSelection) {
                    selectFirstItemIfNeed()
                }
            }
        }
    }

    override fun onSelect(item: SelectableItem) {
        if (canItemBeSelected(item)) {
            setItemSelected(item)
        }
    }

    private fun notifyItemsSelected(items: Set<SelectableItem>, isSelectedByUser: Boolean) {
        items.forEach { notifyItemSelected(it, isSelectedByUser) }
    }

    private fun notifyItemSelected(item: SelectableItem, isSelectedByUser: Boolean) {
        notifyOnSelected((item as Model).item, item.isSelected(), isSelectedByUser)
    }

    fun clear() {
        selectedList.forEach { item ->
            updateItemWithState(item, false)
        }
        selectedList.clear()
    }
}