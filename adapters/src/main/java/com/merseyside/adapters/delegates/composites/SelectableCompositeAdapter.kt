@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.callback.OnSelectEnabledListener
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.delegates.SimpleDelegatesManager
import com.merseyside.adapters.interfaces.selectable.ISelectableAdapter
import com.merseyside.adapters.interfaces.selectable.SelectableMode
import com.merseyside.adapters.model.SelectableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class SelectableCompositeAdapter<Parent, Model>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Parent, Model> = SimpleDelegatesManager(),
    selectableMode: SelectableMode = SelectableMode.SINGLE,
    override var isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE,
    isSelectEnabled: Boolean = true,
) : SortedCompositeAdapter<Parent, Model>(scope, delegatesManager),
    ISelectableAdapter<Parent, Model>
        where Model : SelectableAdapterParentViewModel<out Parent, Parent> {

    override var selectedList: MutableList<Model> = ArrayList()
    override val selectedListeners: MutableList<OnItemSelectedListener<Parent>> = ArrayList()
    override var onSelectEnableListener: OnSelectEnabledListener? = null

    override var isGroupAdapter: Boolean = false

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

                models.isNotNullAndEmpty {
                    forEach { model ->
                        model.isSelectable = value
                    }
                }
            }
        }

    override val internalOnSelect: (Parent) -> Unit = { item ->
        doAsync {
            val model = getModelByItem(item)
            model?.let {
                if (model.isSelectable) {
                    setModelSelected(model, true)
                }
            }
        }
    }

    override suspend fun setModelSelected(model: Model?, isSelectedByUser: Boolean): Boolean {
        return if (super.setModelSelected(model, isSelectedByUser)) {
            recyclerView?.invalidateItemDecorations()
            true
        } else false
    }

    override fun removeListeners() {
        super.removeListeners()
        selectedListeners.clear()
    }
}