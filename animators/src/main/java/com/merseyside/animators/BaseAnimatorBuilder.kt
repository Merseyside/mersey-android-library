package com.merseyside.animators

import android.animation.Animator
import com.merseyside.utils.time.TimeUnit

abstract class BaseAnimatorBuilder<T, M>(
    val duration: TimeUnit
) {

    var onValueCallback: (M) -> Unit = {}

    var isReverse: Boolean = false
        internal set

    internal abstract fun build(): Animator

    internal abstract fun getCurrentValue(): Any

    internal abstract fun calculateCurrentValue(): Any

    companion object {
        internal const val CURRENT_FLOAT = 9999F
        internal const val CURRENT_INT = 9999
    }
}