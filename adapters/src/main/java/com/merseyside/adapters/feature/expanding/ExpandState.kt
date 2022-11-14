package com.merseyside.adapters.feature.expanding

import androidx.databinding.ObservableBoolean
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField
import com.merseyside.merseyLib.kotlin.observable.SingleObservableEvent
import com.merseyside.merseyLib.kotlin.observable.combineFields

class ExpandState(
    expanded: Boolean = false,
    expandable: Boolean = true
) {

    internal var globalExpandable = MutableObservableField(true)
    private val itemExpandable = MutableObservableField(expandable)

    val expandedObservable = ObservableBoolean(expanded)
    val expandableObservable = ObservableBoolean(expandable)

    internal val expandEvent = SingleObservableEvent()

    val expandableField: ObservableField<Boolean> = combineFields(
        globalExpandable, itemExpandable
    ) { first, second -> first && second }

    private var listener: OnExpandStateListener? = null

    var expanded: Boolean = expanded
        internal set(value) {
            if (field != value) {
                field = value

                expandedObservable.set(value)
                listener?.onExpanded(value)
            }
        }

    var expandable: Boolean
        get() = expandableField.value!!
        internal set(value) { itemExpandable.value = value }

    init {
        expandableField.observe { value ->
            expandableObservable.set(value)
            listener?.onExpandable(value)
        }
    }

    fun setOnExpandStateListener(listener: OnExpandStateListener) {
        this.listener = listener
    }

    fun onExpand() {
        expandEvent.call()
    }

    fun onExpand(state: Boolean) {
        if (expanded != state) onExpand()
    }

    interface OnExpandStateListener {
        fun onExpanded(expanded: Boolean)

        fun onExpandable(expandable: Boolean)
    }
}