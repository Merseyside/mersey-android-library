package com.merseyside.adapters.model

import androidx.annotation.CallSuper
import androidx.databinding.BaseObservable
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.base.ItemPositionInterface

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

    /**
     * Use this method with custom lambda databinding methods.
     * https://discuss.kotlinlang.org/t/using-lambda-in-custom-bindingadapter-using-android-databinding-and-kotlin/4229
     */
    @CallSuper
    open fun onClickVoid(): Void? {
        onClick()
        return null as Void?
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

    open fun onPositionChanged(position: Int) {}

    open fun isDeletable(): Boolean {
        return true
    }

    open fun onRecycled() {}

    abstract fun areItemsTheSame(obj: M): Boolean

    fun areItemsNotTheSame(obj: M) = !areItemsTheSame(obj)

    abstract fun notifyUpdate()

}
