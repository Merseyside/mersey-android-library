package com.merseyside.adapters.callback

interface OnItemClickListener<M> {

    fun onItemClicked(obj: M)
}

interface HasOnItemClickListener<M> {
    var listener: OnItemClickListener<M>?

    fun setOnItemClickListener(listener: OnItemClickListener<M>?) {
        this.listener = listener
    }
}