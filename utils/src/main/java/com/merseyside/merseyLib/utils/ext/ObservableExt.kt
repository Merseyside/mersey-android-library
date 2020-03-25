package com.merseyside.merseyLib.utils.ext

import androidx.databinding.Observable
import androidx.databinding.ObservableField

fun <T> ObservableField<T>.onChange(
    onChange: (property: ObservableField<T>, value: T?, isInitial: Boolean) -> Unit
): Observable.OnPropertyChangedCallback {
    val callback = object: Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            onChange.invoke(this@onChange, this@onChange.get(), false)
        }
    }

    val initialValue = this@onChange.get()

    this.addOnPropertyChangedCallback(callback).also {
        if (initialValue != null) {
            onChange.invoke(this, initialValue, true)
        }
    }

    return callback
}