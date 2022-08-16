package com.merseyside.adapters.feature.compare

import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.merseyLib.kotlin.extensions.isNotZero

abstract class PrioritizedComparator<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>> :
    Comparator<Parent, Model>() {

    override fun compare(model1: Model, model2: Model): Int {
        val result = comparePriority(model1.priority, model2.priority)
        return if (result.isNotZero()) result
        else throw IllegalArgumentException(
            "Both models have equal priority." +
                    " Override this method in order to compare by another params."
        )
    }

    open fun comparePriority(priority1: Int, priority2: Int): Int {
        return priority1.compareTo(priority2)
    }
}