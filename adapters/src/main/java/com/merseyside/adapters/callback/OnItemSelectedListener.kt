package com.merseyside.adapters.callback

interface OnItemSelectedListener<M> {
    fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean)
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