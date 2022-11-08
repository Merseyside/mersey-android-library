package com.merseyside.adapters.feature.sorting

import com.merseyside.adapters.model.VM
import com.merseyside.adapters.utils.AdapterWorkManager
import com.merseyside.utils.reflection.ReflectionUtils

abstract class Comparator<Parent, Model : VM<Parent>>(
    protected var animation: Boolean = true
) {

    private lateinit var callback: OnComparatorUpdateCallback
    internal lateinit var workManager: AdapterWorkManager

    abstract fun compare(model1: Model, model2: Model): Int

    fun updateAsync(onComplete: (Unit) -> Unit = {}) {
        workManager.doAsync(onComplete) {
            update()
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

    open fun getModelClass(): Class<*> {
        return ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            Comparator::class.java,
            1
        )
    }
}