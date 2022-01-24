package com.merseyside.adapters.callback

import com.merseyside.adapters.utils.SelectableAdapterListUtils

interface OnItemSelectedListener<M> {
    fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean)
    fun onSelectedRemoved(adapterList: SelectableAdapterListUtils<M, *>, items: List<M>)
}

interface OnSelectEnabledListener {
    fun onEnabled(isEnabled: Boolean)
}

interface HasOnItemSelectedListener<M> {
    val selectedListeners: MutableList<OnItemSelectedListener<M>>

    fun addOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        this.selectedListeners.add(listener)
    }

    fun removeOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        this.selectedListeners.remove(listener)
    }
}