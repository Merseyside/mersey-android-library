package com.merseyside.adapters.callback

import com.merseyside.adapters.base.SelectableAdapter

interface OnItemSelectedListener<M : Any> {
    fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean)
    fun onSelectedRemoved(adapter: SelectableAdapter<M, *>, items: List<M>)
}

interface OnSelectEnabledListener {
    fun onEnabled(isEnabled: Boolean)
}

interface HasOnItemSelectedListener<M : Any> {
    val selectedListeners: MutableList<OnItemSelectedListener<M>>

    fun addOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        this.selectedListeners.add(listener)
    }

    fun removeOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        this.selectedListeners.remove(listener)
    }
}