package com.merseyside.adapters.callback

interface OnItemClickListener<Item> {

    fun onItemClicked(item: Item)
}

interface HasOnItemClickListener<Item> {
    var clickListeners: MutableList<OnItemClickListener<Item>>

    fun setOnItemClickListener(listener: OnItemClickListener<Item>) {
        clickListeners.add(listener)
    }

    fun removeOnItemClickListener(listener: OnItemClickListener<Item>) {
        clickListeners.remove(listener)
    }

    fun removeAllClickListeners() {
        clickListeners.clear()
    }
}