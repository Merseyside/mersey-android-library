package com.merseyside.animators.template

import com.merseyside.animators.BaseAnimator

abstract class AnimatorTemplate {

    abstract fun createAnimator(): BaseAnimator

    val animator: BaseAnimator by lazy { createAnimator() }

    fun start() {
        animator.start()
    }

    fun reverse() {
        animator.reverse()
    }
}