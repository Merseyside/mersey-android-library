package com.merseyside.adapters.model

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.merseyside.utils.ext.onChange
import com.merseyside.utils.mainThreadIfNeeds

abstract class SelectableAdapterParentViewModel<Item : Parent, Parent>(
    item: Item,
    isSelected: Boolean = IS_SELECTED_DEFAULT,
    isSelectable: Boolean = IS_SELECTABLE_DEFAULT
) : ComparableAdapterParentViewModel<Item, Parent>(item) {

    internal lateinit var onSelectCallback: (SelectableAdapterParentViewModel<Item, Parent>) -> Unit

    val selectableObservable = ObservableBoolean()
    val selectEnabledObservable = ObservableBoolean(isSelectable)

    var isSelected: Boolean = isSelected
        internal set(value) {
            if (field != value) {
                field = value

                mainThreadIfNeeds {
                    this.selectedObservable.set(isSelected)
                    onSelectedChanged(isSelected)
                }
            }
        }

    var isSelectable: Boolean = isSelectable
        internal set(value) {
            if (field != value) {
                field = value

                mainThreadIfNeeds {
                    this.selectableObservable.set(isSelectable)
                    onSelectableChanged(isSelectable)
                }
            }
        }


    val selectedObservable = ObservableField(isSelected).apply {
        onChange { _, value, isInitial ->
            if (!isInitial && value != isSelected) {
                onClick()
            }
        }
    }

    open fun onSelect() {
        onSelectCallback(this)
    }

    /**
     * Notify item isSelectEnabled in adapter has changed.
     */
    open fun notifySelectEnabled(isEnabled: Boolean) {}

    /**
     * Notify item that select state has changed.
     */
    open fun onSelectedChanged(isSelected: Boolean) {
        notifyUpdate()
    }

    /**
     * Notify item that selectable state has changed.
     */
    open fun onSelectableChanged(isSelectable: Boolean) {}

    companion object {
        private const val IS_SELECTED_DEFAULT = false
        private const val IS_SELECTABLE_DEFAULT = true
    }
}