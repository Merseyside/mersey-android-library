package com.merseyside.merseyLib.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseSelectableAdapterViewModel<M>(
    obj: M,
    isSelected: Boolean = false,
    isSelectable: Boolean = true
) : BaseComparableAdapterViewModel<M>(obj) {

    private var isSelected = MutableLiveData<Boolean>()

    fun getSelected(): LiveData<Boolean> = isSelected

    private var isSelectable = MutableLiveData<Boolean>()

    fun getSelectable(): LiveData<Boolean> = isSelectable

    init {
        setSelectable(isSelectable)
        setSelected(isSelected)
    }

    fun setSelected(isSelected: Boolean, isNotifyItem: Boolean = false) {
        if (isSelectable() && isSelected() != isSelected) {
            this.isSelected.value = isSelected

            if (isNotifyItem) {
                onSelectedChanged(isSelected)
            }
        }
    }

    fun setSelectable(isSelectable: Boolean, isNotifyItem: Boolean = false) {
        if (isSelectable() != isSelectable) {
            this.isSelectable.value = isSelectable

            if (isNotifyItem) {
                onSelectableChanged(isSelectable)
            }
        }
    }

    fun isSelected(): Boolean {
        return isSelected.value ?: false
    }

    fun isSelectable(): Boolean {
        return isSelectable.value ?: false
    }

    abstract fun onSelectedChanged(isSelected: Boolean)

    open fun onSelectableChanged(isSelectable: Boolean) {}
}