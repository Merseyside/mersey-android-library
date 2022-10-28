package com.merseyside.adapters.feature.selecting

import androidx.databinding.ObservableBoolean
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField
import com.merseyside.merseyLib.kotlin.observable.combineFields

class SelectState(
    selected: Boolean = false,
    selectable: Boolean = true
) {

    internal var globalSelectable = MutableObservableField(true)
    internal val itemSelectableField = MutableObservableField(selectable)


    private var listener: OnSelectStateListener? = null

    interface OnSelectStateListener {
        fun onSelected(selected: Boolean)

        fun onSelectable(selectable: Boolean)
    }

    val selectedObservable = ObservableBoolean(selected)
    val selectableField: ObservableField<Boolean> = combineFields(
        globalSelectable, itemSelectableField) { first, second -> first && second }

    var selected: Boolean = selected
        set(value) {
            if (field != value) {
                field = value

                selectedObservable.set(value)
                listener?.onSelected(value)
            }
        }

    var selectable: Boolean
        get() = selectableField.value!!
        set(value) { itemSelectableField.value = value }

    fun setOnSelectStateListener(listener: OnSelectStateListener) {
        this.listener = listener
    }

    init {
        selectableField.observe { value ->
            listener?.onSelectable(value)
        }
    }
}