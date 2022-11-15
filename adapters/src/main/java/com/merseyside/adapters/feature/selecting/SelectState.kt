package com.merseyside.adapters.feature.selecting

import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.SingleObservableEvent
import com.merseyside.merseyLib.kotlin.observable.ObservableField
import com.merseyside.merseyLib.kotlin.observable.combineFields

class SelectState(
    selected: Boolean = false,
    selectable: Boolean = true
) {

    internal var globalSelectable = MutableObservableField(true)
    private val itemSelectable = MutableObservableField(selectable)

    internal val selectEvent = SingleObservableEvent()

    val selectedObservable = MutableObservableField(selected)
    val selectableObservable: ObservableField<Boolean> = combineFields(
        globalSelectable, itemSelectable
    ) { first, second ->
        first && second
    }

    private var listener: OnSelectStateListener? = null

    var selected: Boolean = selected
        internal set(value) {
            if (field != value) {
                field = value

                selectedObservable.value = value
                listener?.onSelected(value)
            }
        }

    var selectable: Boolean
        get() = selectableObservable.value!!
        internal set(value) { itemSelectable.value = value }

    init {
        selectableObservable.observe { value ->
            listener?.onSelectable(value)
        }
    }

    fun onSelect() {
        selectEvent.call()
    }

    fun onSelect(state: Boolean) {
        if (selected != state) onSelect()
    }

    fun setOnSelectStateListener(listener: OnSelectStateListener) {
        this.listener = listener
    }

    interface OnSelectStateListener {
        fun onSelected(selected: Boolean)

        fun onSelectable(selectable: Boolean)
    }
}