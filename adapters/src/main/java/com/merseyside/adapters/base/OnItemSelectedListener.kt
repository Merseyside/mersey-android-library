package com.merseyside.adapters.base

interface OnItemSelectedListener<M> {
    fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean)
}

interface OnSelectEnabledListener {
    fun onEnabled(isEnabled: Boolean)
}

interface HasOnItemSelectedListener<M> {
    val selectedListeners: MutableList<OnItemSelectedListener<M>>

    fun setOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        this.selectedListeners.add(listener)
    }
}