package com.merseyside.adapters.feature.selecting

import androidx.databinding.ObservableBoolean
import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField
import com.merseyside.merseyLib.kotlin.observable.combineFields

class SelectState(
    selected: Boolean = false,
    selectable: Boolean = true
) {

    internal var globalSelectable = MutableObservableField(true)
    private val itemSelectable = MutableObservableField(selectable)

    val selectedObservable = ObservableBoolean(selected)
    val selectableObservable = ObservableBoolean(selectable)

    private val selectableField: ObservableField<Boolean> = combineFields(
        globalSelectable, itemSelectable
    ) { first, second -> first && second }

    private var listener: OnSelectStateListener? = null

    var selected: Boolean = selected
        internal set(value) {
            if (field != value) {
                field = value

                selectedObservable.set(value)
                listener?.onSelected(value)
            }
        }

    var selectable: Boolean
        get() = selectableField.value!!
        internal set(value) { itemSelectable.value = value }

    init {
        selectableField.observe { value ->
            selectableObservable.set(value)
            listener?.onSelectable(value)
        }
    }

    fun setOnSelectStateListener(listener: OnSelectStateListener) {
        this.listener = listener
    }

    interface OnSelectStateListener {
        fun onSelected(selected: Boolean)

        fun onSelectable(selectable: Boolean)
    }
}