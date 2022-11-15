package com.merseyside.adapters.model

import androidx.annotation.CallSuper
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import com.merseyside.adapters.feature.positioning.PositionHandler
import com.merseyside.merseyLib.kotlin.contract.Identifiable
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.kotlin.observable.ObservableField
import com.merseyside.merseyLib.kotlin.observable.SingleObservableField

@Suppress("UNCHECKED_CAST")
abstract class AdapterParentViewModel<Item : Parent, Parent>(
    item: Item,
    clickable: Boolean = true,
    deletable: Boolean = true,
    filterable: Boolean = true
) : BaseObservable(), PositionHandler {

    var item: Item = item
        internal set

    override var position: Int = NO_ITEM_POSITION

    private val mutClickEvent = SingleObservableField<Item>()
    internal val clickEvent: ObservableField<Item> = mutClickEvent

    val clickableObservable = ObservableBoolean()
    val filterableObservable = ObservableBoolean()
    val deletableObservable = ObservableBoolean()

    open var isClickable: Boolean = clickable
        set(value) {
            if (field != value) {
                field = value

                clickableObservable.set(value)
            }
        }

    open var isFilterable: Boolean = filterable
        set(value) {
            if (field != value) {
                field = value

                filterableObservable.set(value)
            }
        }

    open var isDeletable: Boolean = deletable
        set(value) {
            if (field != value) {
                field = value

                deletableObservable.set(value)
            }
        }

    @CallSuper
    open fun onClick() {
        if (isClickable) {
            mutClickEvent.value = item
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

    open fun onRecycled() {}

    override fun onPositionChanged(fromPosition: Int, toPosition: Int) {}

    internal fun areItemsTheSame(parent: Parent): Boolean {
        return try {
            val another = parent as Item
            areItemsTheSame(another)
        } catch (e: ClassCastException) {
            false
        }
    }

    internal fun areContentsTheSame(parent: Parent): Boolean {
        val other = parent as Item
        return areContentsTheSame(other)
    }

    protected open fun areItemsTheSame(other: Item): Boolean = try {
        (item as Identifiable<*>).getId() == (other as Identifiable<*>).getId()
    } catch (e: ClassCastException) {
        throw NotImplementedError(
            "Items are not Identifiable. " +
                    "Please extend it or override areItemsTheSame()"
        )
    }

    protected open fun areContentsTheSame(other: Item) = item == other

    /**
     * Call notifyPropertyChanged(id) here.
     */
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
        object None : Payloadable
    }

    companion object {
        internal const val NO_ITEM_POSITION = -1
    }
}