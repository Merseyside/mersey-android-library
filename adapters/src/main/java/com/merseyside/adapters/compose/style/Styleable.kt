package com.merseyside.adapters.compose.style

import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.utils.InternalAdaptersApi

@Suppress("UNCHECKED_CAST")
interface Styleable<Definition : ComposingStyle, Binding: ViewDataBinding> {

    @InternalAdaptersApi
    fun applyStyleInternal(binding: ViewDataBinding, styleableItem: StyleableItem<*>) {
        applyStyle(binding as Binding, (styleableItem as StyleableItem<Definition>).composingStyle)
    }

    fun applyStyle(binding: Binding, style: Definition)
}