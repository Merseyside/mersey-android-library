package com.merseyside.adapters.model

import androidx.databinding.ObservableBoolean
import com.merseyside.merseyLib.kotlin.ObservableField
import com.merseyside.merseyLib.kotlin.SingleObservableField

abstract class SelectableAdapterParentViewModel<Item : Parent, Parent>(
    item: Item,
    isSelected: Boolean = IS_SELECTED_DEFAULT,
    isSelectable: Boolean = IS_SELECTABLE_DEFAULT
) : ComparableAdapterParentViewModel<Item, Parent>(item) {

    private val mutSelectEvent = SingleObservableField<Item>()
    internal val selectEvent: ObservableField<Item> = mutSelectEvent

    val selectedObservable = ObservableBoolean(isSelected)
    val selectableObservable = ObservableBoolean()
    val selectEnabledObservable = ObservableBoolean(isSelectable)

    var isSelected: Boolean = isSelected
        set(value) {
            if (field != value) {
                field = value

                selectedObservable.set(isSelected)
                onSelectedChanged(isSelected)
            }
        }

    var isSelectable: Boolean = isSelectable
        set(value) {
            if (field != value) {
                field = value

                selectableObservable.set(value)
                onSelectableChanged(value)
            }
        }

    open fun onSelect() {
        mutSelectEvent.value = item
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