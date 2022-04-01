package com.merseyside.adapters.model

import androidx.annotation.CallSuper
import androidx.databinding.BaseObservable
import com.merseyside.adapters.base.ItemCallback
import com.merseyside.adapters.callback.OnItemClickListener

abstract class AdapterParentViewModel<Item: Parent, Parent>(
    item: Item
): BaseObservable() {

    var item: Item = item
        internal set

    private var pos: Int = NO_ITEM_POSITION
    private lateinit var itemPosition: ItemCallback<AdapterViewModel<Item>>
    internal var priority: Int = 0

    internal fun setItemPositionInterface(i: ItemCallback<AdapterViewModel<Item>>) {
        itemPosition = i
    }

    private val listeners: ArrayList<OnItemClickListener<Parent>>
            by lazy { ArrayList() }

    fun setOnItemClickListener(listener: OnItemClickListener<Parent>) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener)
        }
    }

    fun removeOnItemClickListener(listener: OnItemClickListener<Parent>) {
        if (listeners.isNotEmpty()) {
            listeners.remove(listener).toString()
        }
    }

    @CallSuper
    open fun onClick() {
        if (listeners.isNotEmpty()) {
            listeners.forEach { it.onItemClicked(item) }
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

    internal fun areItemsTheSame(parent: Parent): Boolean {
        return try {
            val another = parent as Item
            areItemsTheSame(another)
        } catch (e: ClassCastException) {
            this.areParentItemsTheSame(parent)
        }
    }

    internal fun areContentsTheSame(parent: Parent): Boolean {
        val another = parent as Item
        return areContentsTheSame(another)
    }

    protected open fun areParentItemsTheSame(parent: Parent): Boolean {
        throw NotImplementedError()
    }

    protected abstract fun areItemsTheSame(other: Item): Boolean
    protected open fun areContentsTheSame(other: Item) = item == other

    fun areItemsNotTheSame(other: Parent) = !this.areItemsTheSame(other)

    open fun notifyUpdate() {}

    internal fun payload(newItem: Parent): List<Payloadable> {
        val payloads = payload(item, newItem as Item)
        this.item = newItem
        notifyUpdate()
        return payloads
    }

    protected open fun payload(oldItem: Item, newItem: Item): List<Payloadable> {
        return listOf(Payloadable.None)
    }

    interface Payloadable {
        object None: Payloadable
    }

    companion object {
        internal const val NO_ITEM_POSITION = -1
    }
}