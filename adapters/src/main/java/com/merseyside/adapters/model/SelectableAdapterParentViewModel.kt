package com.merseyside.adapters.model

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.merseyside.utils.ext.onChange
import com.merseyside.utils.mainThreadIfNeeds

abstract class SelectableAdapterParentViewModel<Item: Parent, Parent>(
    item: Item,
    private var isSelected: Boolean = IS_SELECTED_DEFAULT,
    private var isSelectable: Boolean = IS_SELECTABLE_DEFAULT
) : ComparableAdapterParentViewModel<Item, Parent>(item) {

    private var isSelectEnabled: Boolean = IS_SELECT_ENABLE_DEFAULT

    val selectedObservable = ObservableField(isSelected).apply {
        onChange { _, value, isInitial ->
            if (!isInitial && value != isSelected) {
                onClick()
            }
        }
    }

    val selectableObservable = ObservableBoolean()
    val selectEnabledObservable = ObservableBoolean(isSelectEnabled)

    init {
        setSelectable(isSelectable)
        setSelected(isSelected)
    }

    fun setSelected(isSelected: Boolean, notifyItem: Boolean = true) {
        mainThreadIfNeeds {
            if (this.isSelected != isSelected) {
                this.isSelected = isSelected

                this.selectedObservable.set(isSelected)

                if (notifyItem) {
                    onSelectedChanged(isSelected)
                }
            }
        }
    }

    fun setSelectable(isSelectable: Boolean, isNotifyItem: Boolean = false) {
        mainThreadIfNeeds {
            if (this.isSelectable != isSelectable) {
                this.isSelectable = isSelectable

                this.selectableObservable.set(isSelectable)

                if (isNotifyItem) {
                    onSelectableChanged(isSelectable)
                }
            }
        }
    }

    fun setSelectEnabled(isSelectEnabled: Boolean, isNotifyItem: Boolean = false) {
        mainThreadIfNeeds {
            this.isSelectEnabled = isSelectEnabled

            if (selectEnabledObservable.get() != isSelectEnabled) {
                this.selectEnabledObservable.set(isSelectEnabled)
            }

            if (isNotifyItem) {
                notifySelectEnabled(isSelectEnabled)
            }
        }
    }

    fun isSelected(): Boolean {
        return isSelected
    }

    fun isSelectable(): Boolean {
        return isSelectable
    }

    /**
     * Notify item isSelectEnabled in adapter has changed.
     */
    abstract fun notifySelectEnabled(isEnabled: Boolean)

    /**
     * Notify item that select state has changed.
     */
    abstract fun onSelectedChanged(isSelected: Boolean)

    /**
     * Notify item that selectable state has changed.
     */
    open fun onSelectableChanged(isSelectable: Boolean) {}

    companion object {
        private const val IS_SELECTED_DEFAULT = false
        private const val IS_SELECTABLE_DEFAULT = true
        private const val IS_SELECT_ENABLE_DEFAULT = true
    }
}