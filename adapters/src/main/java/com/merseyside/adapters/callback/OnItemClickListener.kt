package com.merseyside.adapters.callback

interface OnItemClickListener<Item> {

    fun onItemClicked(item: Item)
}

interface HasOnItemClickListener<Item> {
    var listener: OnItemClickListener<Item>?

    fun setOnItemClickListener(listener: OnItemClickListener<Item>?) {
        this.listener = listener
    }
}