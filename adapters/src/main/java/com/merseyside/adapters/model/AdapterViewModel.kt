package com.merseyside.adapters.model

import androidx.annotation.CallSuper
import androidx.databinding.BaseObservable
import com.merseyside.adapters.base.ItemCallback
import com.merseyside.adapters.base.OnItemClickListener

abstract class AdapterViewModel<M>(
    var obj: M
) : BaseObservable() {

    private var pos: Int = NO_ITEM_POSITION
    private lateinit var itemPosition: ItemCallback<AdapterViewModel<M>>

    internal fun setItemPositionInterface(i: ItemCallback<AdapterViewModel<M>>) {
        itemPosition = i
    }

    private val listeners: ArrayList<OnItemClickListener<M>>
            by lazy { ArrayList<OnItemClickListener<M>>() }

    fun setOnItemClickListener(listener: OnItemClickListener<M>) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener)
        }
    }

    fun removeOnItemClickListener(listener: OnItemClickListener<M>) {
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

    fun getItem(): M = obj

    @Throws(IllegalStateException::class)
    fun getPosition(): Int {
        if (pos == NO_ITEM_POSITION) {
            throw IllegalStateException("View has not initialized!")
        }

        return pos
    }
    fun getItemCount() = itemPosition.getItemCount()
    fun isLast() = getPosition() == getItemCount() - 1
    fun isFirst() = getPosition() == 0

    fun onPositionChanged(toPosition: Int) {
        if (this.pos != toPosition) {
            onPositionChanged(fromPosition = this.pos, toPosition = toPosition)
            this.pos = toPosition
        }
    }

    protected open fun onPositionChanged(fromPosition: Int, toPosition: Int) {}

    open fun isDeletable(): Boolean {
        return true
    }

    open fun onRecycled() {}

    abstract fun areItemsTheSame(obj: M): Boolean

    fun areItemsNotTheSame(obj: M) = !areItemsTheSame(obj)

    abstract fun notifyUpdate()

    companion object {
        internal const val NO_ITEM_POSITION = -1
    }
}
