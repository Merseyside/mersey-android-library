package com.merseyside.utils.motionLayout.async

import androidx.constraintlayout.motion.widget.MotionLayout
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun MotionLayout.toStart() = suspendCoroutine { cont ->
    transitionToStart {
        cont.resume(Unit)
    }
}

suspend fun MotionLayout.toEnd() = suspendCoroutine { cont ->
    transitionToEnd {
        cont.resume(Unit)
    }
}