package com.merseyside.animators

import android.animation.Animator
import android.animation.AnimatorSet

/**
 * Collects animators and sets order(Approach) of playing.
 * Default approach is sequential.
 */
class AnimatorList(val approach: Approach = Approach.SEQUENTIALLY): BaseAnimator() {

    internal val list: MutableList<BaseAnimator> = ArrayList()
    private var animatorSet: AnimatorSet? = null

    fun addAnimator(animator: BaseAnimator) {
        list.add(animator)
    }

    override fun getAnimator(): Animator {
        return animatorSet ?: AnimatorSet().apply {
            when (approach) {
                Approach.SEQUENTIALLY -> playSequentially(list.map { it.getAnimator() })
                Approach.TOGETHER -> playTogether(list.map { it.getAnimator() })
            }

            animatorSet = this
        }
    }

    override fun setReverse(isReverse: Boolean) {
        animatorSet = null
        list.forEach { it.setReverse(isReverse) }
    }

    fun isEmpty() = list.isEmpty()

    fun isNotEmpty() = !isEmpty()

    fun clear() {
        list.clear()
        animatorSet = null

        removeAllCallbacks()
        removeAllListeners()
    }
}