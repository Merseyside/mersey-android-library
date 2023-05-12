package com.merseyside.utils.motionLayout.ext

import androidx.constraintlayout.motion.widget.MotionLayout

fun MotionLayout.isInStartState(): Boolean {
    return currentState == startState
}

fun MotionLayout.isInEndState(): Boolean {
    return currentState == endState
}