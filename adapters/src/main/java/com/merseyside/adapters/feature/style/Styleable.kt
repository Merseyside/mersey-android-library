package com.merseyside.adapters.feature.style

import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.utils.InternalAdaptersApi

interface Styleable<Definition : StyleDefinition, Binding: ViewDataBinding> {

    @InternalAdaptersApi
    fun applyStyleInternal(binding: ViewDataBinding, styleableItem: StyleableItem<*>) {
        applyStyle(binding as Binding, (styleableItem as StyleableItem<Definition>).styleDefinition)
    }

    fun applyStyle(binding: Binding, style: Definition)
}