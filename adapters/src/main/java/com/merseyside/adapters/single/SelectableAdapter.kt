package com.merseyside.adapters.single

import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.callback.OnSelectEnabledListener
import com.merseyside.adapters.interfaces.selectable.ISelectableAdapter
import com.merseyside.adapters.interfaces.selectable.SelectableMode
import com.merseyside.adapters.model.SelectableAdapterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class SelectableAdapter<Item, Model: SelectableAdapterViewModel<Item>>(
    selectableMode: SelectableMode = SelectableMode.SINGLE,
    override var isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE,
    isSelectEnabled: Boolean = true,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : SortedAdapter<Item, Model>(scope), ISelectableAdapter<Item, Model> {

    internal var groupAdapter: Boolean = false
    override var selectFirstOnAdd: Boolean = false
        get() {
            return try {
                field
            } finally {
                field = false
            }
        }

    override var selectableMode: SelectableMode = selectableMode
        set(value) {
            if (field != value) {
                field = value

                if (value == SelectableMode.SINGLE) {
                    if (selectedList.size > 1) {
                        (1 until selectedList.size).forEach { index ->
                            selectedList[index].isSelected = false
                        }

                        selectedList = mutableListOf(selectedList.first())
                    }
                }
            }
        }

    override var isSelectEnabled: Boolean = isSelectEnabled
        set(value) {
            if (value != field) {
                field = value

                onSelectEnableListener?.onEnabled(value)

                if (models.isNotEmpty()) {
                    models.forEach { model ->
                        model.isSelectable = value
                    }
                }
            }
        }

    override val internalSelectCallback: (Model) -> Unit = { model ->
        if (this.isSelectEnabled) {
            setModelSelected(model, true)
        }
    }

    override val selectedListeners: MutableList<OnItemSelectedListener<Item>> = ArrayList()
    override var onSelectEnableListener: OnSelectEnabledListener? = null

    override var selectedList: MutableList<Model> = ArrayList()

    override var isGroupAdapter: Boolean = false

    override fun setModelSelected(model: Model?, isSelectedByUser: Boolean): Boolean {
        return if (super.setModelSelected(model, isSelectedByUser)) {
            recyclerView?.invalidateItemDecorations()
            true
        } else false
    }

    companion object {
        const val NO_SELECTIONS = -1
    }
}