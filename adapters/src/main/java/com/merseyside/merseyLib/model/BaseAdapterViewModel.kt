package com.merseyside.merseyLib.model

import androidx.annotation.CallSuper
import androidx.databinding.BaseObservable
import com.merseyside.merseyLib.adapters.BaseAdapter
import com.merseyside.merseyLib.adapters.ItemPositionInterface
import com.merseyside.merseyLib.utils.Logger

abstract class BaseAdapterViewModel<M>(
    obj: M
) : BaseObservable() {

    abstract var obj: M

    private lateinit var itemPosition: ItemPositionInterface<BaseAdapterViewModel<M>>

    internal fun setItemPositionInterface(i: ItemPositionInterface<BaseAdapterViewModel<M>>) {
        itemPosition = i
    }

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

    fun isLast(): Boolean {
        return itemPosition.isLast(this)
    }

    fun isFirst(): Boolean {
        return itemPosition.isFirst(this)
    }

    fun getPosition(): Int {
        return itemPosition.getPosition(this)
    }

    open fun onPositionChanged(position: Int) {
        Logger.log(this, "on position changed $position")
    }

    open fun isDeletable(): Boolean {
        return true
    }

    open fun onRecycled() {}

    abstract fun areItemsTheSame(obj: M): Boolean

    fun areItemsNotTheSame(obj: M) = !areItemsTheSame(obj)

    abstract fun notifyUpdate()

}
