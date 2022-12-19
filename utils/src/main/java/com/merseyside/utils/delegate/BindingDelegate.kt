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
    attachToParent: Boolean = true,
    onBind: B.() -> Unit = {}
): Binding<B> = LazyBindingImpl(layoutRes, this, attachToParent, onBind)

fun <B: ViewDataBinding> ViewGroup.dataBinding(
    @LayoutRes layoutRes: Int,
    variableId: Int,
    data: Any,
    attachToParent: Boolean = true,
    onBind: B.() -> Unit = {}
): Binding<B> = LazyDataBindingImpl(layoutRes, this, attachToParent, variableId, data, onBind)


private open class LazyBindingImpl<B: ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    private val view: ViewGroup,
    private val attachToParent: Boolean,
    private val onBind: B.() -> Unit
): Binding<B> {

    private var _value: B? = null
    override val value: B
        get() {
            return _value!!
        }

    init {
        initBinding()
    }

    protected fun initBinding(): B {
        if (_value == null) {
            return view.getBinding<B>(layoutRes, attachToParent).also {
                _value = it
                onBind(it)
            }
        } else {
            throw IllegalStateException("Binding already initialized")
        }
    }
}

private class LazyDataBindingImpl<B: ViewDataBinding>(
    @LayoutRes layoutRes: Int,
    view: ViewGroup,
    attachToParent: Boolean,
    private val variableId: Int,
    private val data: Any,
    private val onBind: B.() -> Unit
): LazyBindingImpl<B>(layoutRes, view, attachToParent, onBind) {

    init {
        initDataBinding()
    }

    private fun initDataBinding(): B {
        return value.apply {
            setVariable(variableId, data)
        }
    }
}

fun <B: ViewDataBinding> ViewGroup.getBinding(@LayoutRes layoutRes: Int, attachToParent: Boolean = true): B {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return DataBindingUtil.inflate(inflater, layoutRes, this, attachToParent)
}