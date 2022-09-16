package com.merseyside.adapters.feature.compositeScreen.delegate

import com.merseyside.adapters.feature.compare.Priority.validatePriority
import com.merseyside.adapters.feature.compositeScreen.SCV
import com.merseyside.adapters.feature.compositeScreen.StyleableComposingView
import com.merseyside.adapters.feature.style.ComposingStyle
import com.merseyside.adapters.interfaces.delegate.IPrioritizedDelegateAdapter
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class PrioritizedViewDelegateAdapter<View : StyleableComposingView<Style>, Style : ComposingStyle, Model>(
    priority: Int = 0
) : ViewDelegateAdapter<View, Style, Model>(),
    IPrioritizedDelegateAdapter<View, SCV, Model>
        where Model : ComparableAdapterParentViewModel<View, SCV> {

    override var priority: Int = priority
        set(value) {
            if (field != value) {
                validatePriority(value)
                field = value
            }
        }

    @OptIn(InternalAdaptersApi::class)
    override fun onModelCreated(model: Model) {
        return super.onModelCreated(model).also { model.priority = priority }
    }
}