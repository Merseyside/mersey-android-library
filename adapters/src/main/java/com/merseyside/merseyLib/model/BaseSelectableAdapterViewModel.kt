package com.merseyside.merseyLib.model

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.merseyside.merseyLib.utils.Logger
import com.merseyside.merseyLib.utils.ext.onChange
import com.merseyside.merseyLib.utils.mainThreadIfNeeds

abstract class BaseSelectableAdapterViewModel<M>(
    obj: M,
    private var isSelected: Boolean = IS_SELECTED_DEFAULT,
    private var isSelectable: Boolean = IS_SELECTABLE_DEFAULT
) : BaseComparableAdapterViewModel<M>(obj) {

    private var isSelectEnabled: Boolean = IS_SELECT_ENABLE_DEFAULT

    var selectedObservable = ObservableField(isSelected).apply {
        onChange { _, value, isInitial ->
            if (!isInitial && value != isSelected) {
                onClick()
            }
        }
    }

    var selectableObservable = ObservableBoolean()
    var selectEnabledObservable = ObservableBoolean(isSelectEnabled)

    init {
        setSelectable(isSelectable)
        setSelected(isSelected)
    }

    fun setSelected(isSelected: Boolean, isNotifyItem: Boolean = false) {
        mainThreadIfNeeds {
            this.isSelected = isSelected

            if (isSelectedObservable() != isSelected) {
                this.selectedObservable.set(isSelected)
            }

            if (isNotifyItem) {
                onSelectedChanged(isSelected)
            }
        }
    }

    fun setSelectable(isSelectable: Boolean, isNotifyItem: Boolean = false) {
        mainThreadIfNeeds {
            this.isSelectable = isSelectable

            if (isSelectableObservable() != isSelectable) {
                this.selectableObservable.set(isSelectable)
            }

            if (isNotifyItem) {
                onSelectableChanged(isSelectable)
            }
        }
    }

    fun setSelectEnabled(isSelectEnabled: Boolean, isNotifyItem: Boolean = false) {
        mainThreadIfNeeds {
            this.isSelectEnabled = isSelectEnabled

            if (isSelectEnabledObservable() != isSelectEnabled) {
                this.selectEnabledObservable.set(isSelectEnabled)
            }

            if (isNotifyItem) {
                notifySelectEnabled(isSelectEnabled)
            }
        }
    }

    private fun isSelectedObservable(): Boolean {
        return selectedObservable.get() ?: IS_SELECTED_DEFAULT
    }

    private fun isSelectableObservable(): Boolean {
        return selectableObservable.get() ?: IS_SELECTED_DEFAULT
    }

    private fun isSelectEnabledObservable(): Boolean {
        return selectEnabledObservable.get() ?: IS_SELECT_ENABLE_DEFAULT
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