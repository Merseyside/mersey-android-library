@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.base.SelectableAdapter
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.callback.OnSelectEnabledListener
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.model.SelectableAdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.SelectableAdapterListUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class SelectableCompositeAdapter<Parent, Model: SelectableAdapterViewModel<Parent>>(
    delegatesManager: DelegatesManager<Parent, Model> = DelegatesManager(),
    selectableMode: SelectableAdapter.SelectableMode = SelectableAdapter.SelectableMode.SINGLE,
    override var isAllowToCancelSelection: Boolean =
        selectableMode == SelectableAdapter.SelectableMode.MULTIPLE,
    isSelectEnabled: Boolean = true,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : SortedCompositeAdapter<Parent, Model>(delegatesManager, scope),
    SelectableAdapterListUtils<Parent, Model> {

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

    override var selectableMode: SelectableAdapter.SelectableMode = selectableMode
        set(value) {
            if (field != value) {
                field = value

                if (value == SelectableAdapter.SelectableMode.SINGLE) {
                    if (selectedList.size > 1) {
                        (1 until selectedList.size).forEach { index ->
                            selectedList[index].setSelected(false)
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

                if (modelList.isNotEmpty()) {
                    modelList.forEach { model ->
                        model.setSelectEnabled(value)
                    }
                }
            }
        }

    override fun onModelCreated(model: Model) {
        super.onModelCreated(model)
        model.setSelectEnabled(isSelectEnabled)
    }

    override fun setItemSelected(item: Model?, isSelectedByUser: Boolean): Boolean {
        return if (super.setItemSelected(item, isSelectedByUser)) {
            recyclerView?.invalidateItemDecorations()
            true
        } else false
    }

    override fun removeListeners() {
        super.removeListeners()
        selectedListeners.clear()
    }
}