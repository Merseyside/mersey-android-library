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

fun ObservableField<*>.isNull(): Boolean {
    return get() == null
}

fun ObservableField<*>.isNotNull() = !isNull()

inline fun <reified T : Any> ObservableField<T>.isNotNullAndEmpty() = isNotNull() && isNotEmpty()

inline fun <reified T : Any> ObservableField<T>.isEmpty(): Boolean {
    return if (isNotNull()) {

        when {
            String is T -> {
                (get() as String).isEmpty()
            }
            T::class is Collection<*> -> {
                (get() as Collection<*>).isEmpty()
            }
            else -> false
        }

    } else {
        true
    }
}

inline fun <reified T : Any> ObservableField<T>.isNotEmpty() = !isEmpty()