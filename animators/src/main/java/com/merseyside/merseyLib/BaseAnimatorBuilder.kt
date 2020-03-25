package com.merseyside.merseyLib

import android.animation.Animator
import android.view.View
import com.merseyside.merseyLib.utils.Logger
import com.merseyside.merseyLib.utils.time.TimeUnit

abstract class BaseAnimatorBuilder<T>(
    val view: View,
    val duration: TimeUnit
) {

    var isReverse: Boolean = false
        internal set

    internal abstract fun build(): Animator

    abstract fun getCurrentValue(): Any

    abstract fun calculateCurrentValue(): Any

    companion object {
        internal const val CURRENT_FLOAT = 9999F
        internal const val CURRENT_INT = 9999
    }
}