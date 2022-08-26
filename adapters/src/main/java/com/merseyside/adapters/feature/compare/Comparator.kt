package com.merseyside.adapters.feature.compare

import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineWorkManager

abstract class Comparator<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>> {

    private lateinit var callback: OnComparatorUpdateCallback
    internal lateinit var workManager: CoroutineWorkManager<Any, Unit>

    abstract fun compare(model1: Model, model2: Model): Int

    fun updateAsync(onComplete: () -> Unit = {}) {
        workManager.addAndExecute {
            update()
            onComplete()
        }
    }

    suspend fun update() {
        callback.onUpdate()
    }

    internal fun setOnComparatorUpdateCallback(callback: OnComparatorUpdateCallback) {
        this.callback = callback
    }

    internal interface OnComparatorUpdateCallback {
        suspend fun onUpdate()
    }
}