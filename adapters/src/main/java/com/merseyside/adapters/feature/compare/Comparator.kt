package com.merseyside.adapters.feature.compare

import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue

abstract class Comparator<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>(
    protected var animation: Boolean = true
) {

    private lateinit var callback: OnComparatorUpdateCallback
    internal lateinit var workManager: CoroutineQueue<Any, Unit>

    abstract fun compare(model1: Model, model2: Model): Int

    fun updateAsync(onComplete: () -> Unit = {}) {
        workManager.addAndExecute {
            update()
            onComplete()
        }
    }

    suspend fun update() {
        callback.onUpdate(animation)
    }

    internal fun setOnComparatorUpdateCallback(callback: OnComparatorUpdateCallback) {
        this.callback = callback
    }

    internal interface OnComparatorUpdateCallback {
        suspend fun onUpdate(animation: Boolean)
    }
}