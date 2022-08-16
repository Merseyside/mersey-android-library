package com.merseyside.adapters.feature.compare

import com.merseyside.adapters.model.ComparableAdapterParentViewModel

abstract class Comparator<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>> {

    private lateinit var callback: OnComparatorUpdateCallback

    abstract fun compare(model1: Model, model2: Model): Int

    fun update() {
        callback.onUpdate()
    }

    internal fun setOnComparatorUpdateCallback(callback: OnComparatorUpdateCallback) {
        this.callback = callback
    }

    internal interface OnComparatorUpdateCallback {
        fun onUpdate()
    }
}