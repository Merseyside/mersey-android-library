package com.merseyside.adapters.callback

import com.merseyside.adapters.interfaces.selectable.ISelectableAdapter

interface OnItemSelectedListener<Item> {
    fun onSelected(item: Item, isSelected: Boolean, isSelectedByUser: Boolean)
    fun onSelectedRemoved(adapterList: ISelectableAdapter<Item, *>, items: List<Item>)
}

interface OnSelectEnabledListener {
    fun onEnabled(isEnabled: Boolean)
}

interface HasOnItemSelectedListener<Item> {
    val selectedListeners: MutableList<OnItemSelectedListener<Item>>

    fun addOnItemSelectedListener(listener: OnItemSelectedListener<Item>) {
        this.selectedListeners.add(listener)
    }

    fun removeOnItemSelectedListener(listener: OnItemSelectedListener<Item>) {
        this.selectedListeners.remove(listener)
    }

    fun notifyOnSelected(item: Item, isSelected: Boolean, isSelectedByUser: Boolean) {
        selectedListeners.forEach { listener ->
            listener.onSelected(item, isSelected, isSelectedByUser)
        }
    }
}