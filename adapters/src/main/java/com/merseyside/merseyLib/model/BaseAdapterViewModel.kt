package com.merseyside.merseyLib.model

import androidx.annotation.CallSuper
import androidx.databinding.BaseObservable
import com.merseyside.merseyLib.adapters.BaseAdapter

abstract class BaseAdapterViewModel<M>(
    obj: M
) : BaseObservable() {

    abstract var obj: M

    init {
        this.obj = obj
    }

    private val listeners: ArrayList<BaseAdapter.OnItemClickListener<M>>
            by lazy { ArrayList<BaseAdapter.OnItemClickListener<M>>() }

    fun setOnItemClickListener(listener: BaseAdapter.OnItemClickListener<M>) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener)
        }
    }

    fun removeOnItemClickListener(listener: BaseAdapter.OnItemClickListener<M>) {
        if (listeners.isNotEmpty()) {
            listeners.remove(listener).toString()
        }
    }

    @CallSuper
    open fun onClick() {
        if (listeners.isNotEmpty()) {
            listeners.forEach { it.onItemClicked(obj) }
        }
    }

    open fun setItem(item: M) {
        this.obj = item
        notifyUpdate()
    }

    fun getItem(): M {
        return obj
    }

    open fun onRecycled() {}

    abstract fun areItemsTheSame(obj: M): Boolean

    abstract fun notifyUpdate()

    companion object {
        private const val TAG = "BaseAdapterViewModel"
    }
}
