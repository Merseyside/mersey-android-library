package com.merseyside.utils.delegate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Delegate applicable for adding binding feature in custom views
 * Use it like 'binding by viewBinding(layoutId, attachToParent)
 */

private interface Binding<out T : ViewDataBinding> {
    val value: T
}

fun <B : ViewDataBinding> ViewGroup.viewBinding(
    @LayoutRes layoutRes: Int,
    attachToParent: Boolean = true,
    onBind: B.() -> Unit = {}
) = lazy { createViewBinding(layoutRes, attachToParent, onBind) }


fun <B : ViewDataBinding> ViewGroup.createViewBinding(
    @LayoutRes layoutRes: Int,
    attachToParent: Boolean = true,
    onBind: B.() -> Unit = {}
): B = LazyBindingImpl(layoutRes, this, attachToParent, onBind).value

/* Data Binding */
fun <B : ViewDataBinding> ViewGroup.dataBinding(
    @LayoutRes layoutRes: Int,
    variableId: Int,
    data: Any,
    attachToParent: Boolean = true,
    onBind: B.() -> Unit = {}
) = lazy { createDataBinding(layoutRes, variableId, data, attachToParent, onBind) }

fun <B : ViewDataBinding> ViewGroup.createDataBinding(
    @LayoutRes layoutRes: Int,
    variableId: Int,
    data: Any,
    attachToParent: Boolean = true,
    onBind: B.() -> Unit = {}
): B = LazyDataBindingImpl(layoutRes, this, attachToParent, variableId, data, onBind).value


private open class LazyBindingImpl<B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    view: ViewGroup,
    attachToParent: Boolean,
    onBind: B.() -> Unit
) : Binding<B> {

    private var _value: B? = null
    override val value: B
        get() = requireNotNull(_value)

    init {
        view.getBinding<B>(layoutRes, attachToParent).also { value ->
            _value = value
            onBind(value)
        }
    }
}

private class LazyDataBindingImpl<B : ViewDataBinding>(
    @LayoutRes layoutRes: Int,
    view: ViewGroup,
    attachToParent: Boolean,
    variableId: Int,
    data: Any,
    onBind: B.() -> Unit
) : LazyBindingImpl<B>(layoutRes, view, attachToParent, onBind) {

    init {
        value.setVariable(variableId, data)
    }
}

fun <B : ViewDataBinding> ViewGroup.getBinding(
    @LayoutRes layoutRes: Int,
    attachToParent: Boolean = true
): B {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return DataBindingUtil.inflate(inflater, layoutRes, this, attachToParent)
}