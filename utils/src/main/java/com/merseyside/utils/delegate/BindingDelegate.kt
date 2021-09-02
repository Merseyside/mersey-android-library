package com.merseyside.utils.delegate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.reflect.KProperty

/**
 * Delegate applicable for adding binding feature in custom views
 * Use it like 'binding by viewBinding(layoutId, attachToParent)
 *
 * Sometimes you have to import getValue explicitly by adding
 * import com.merseyside.utils.delegate.getValue
 */

interface Binding<out T: ViewDataBinding> {
    val value: T
}

operator fun <B : ViewDataBinding> Binding<B>.getValue(view: ViewGroup, property: KProperty<*>): B = value

fun <B: ViewDataBinding> ViewGroup.viewBinding(
    @LayoutRes layoutRes: Int,
    attachToParent: Boolean = true
): Binding<B> =
    LazyBindingImpl(layoutRes, this, attachToParent)

private class LazyBindingImpl<B: ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    private val view: ViewGroup,
    private val attachToParent: Boolean
): Binding<B> {

    private var _value: B? = null

    override val value: B
        get() {
            return _value!!
        }

    init {
        initBinding()
    }

    private fun initBinding(): B {
        if (_value == null) {
            return view.getBinding<B>(layoutRes, attachToParent).also { _value = it }
        } else {
            throw IllegalStateException("Binding already initialized")
        }
    }
}

fun <B: ViewDataBinding> ViewGroup.getBinding(@LayoutRes layoutRes: Int, attachToParent: Boolean = true): B {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return DataBindingUtil.inflate(inflater, layoutRes, this, attachToParent)
}