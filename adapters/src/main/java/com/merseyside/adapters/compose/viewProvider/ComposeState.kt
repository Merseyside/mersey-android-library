package com.merseyside.adapters.compose.viewProvider

import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField

abstract class ComposeState<T>(val propertyName: String) {

    internal abstract val observableField: ObservableField<T>

    abstract val value: T?

    internal fun observe(observer: (T) -> Unit) {
        observableField.observe(ignoreCurrent = true, observer)
    }
}

class MutableComposeState<T>(propertyName: String, initValue: T): ComposeState<T>(propertyName) {

    override val observableField = MutableObservableField(initValue)

    override var value: T?
        get() = observableField.value
        set(value) {
            if (observableField.value != value) {
                observableField.value = value
            }
        }
}