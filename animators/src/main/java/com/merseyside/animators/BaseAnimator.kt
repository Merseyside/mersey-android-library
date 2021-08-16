package com.merseyside.animators

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Build
import com.merseyside.merseyLib.time.Millis
import com.merseyside.utils.Logger
import com.merseyside.utils.delayedMainThread
import com.merseyside.utils.emptyMutableList
import com.merseyside.utils.ext.log

abstract class BaseAnimator {

    var isLogging = false

    private var isLegacy = Build.VERSION.SDK_INT < Build.VERSION_CODES.O
    private var isReverse = false

    set(value) {
        field = value

        if (isLegacy) {
            setReverse(value)
        }
    }

    private val listenerList: MutableList<Animator.AnimatorListener> by lazy { emptyMutableList() }

    fun getListeners(): List<Animator.AnimatorListener> = listenerList

    private var internalCallback: Animator.AnimatorListener? = null

    private var onEndCallback: (animation: Animator?, isReverse: Boolean) -> Unit? = { _, _ -> }
    private var onRepeatCallback: (animation: Animator?, isReverse: Boolean) -> Unit? = { _, _ -> }
    private var onCancelCallback: (animation: Animator?, isReverse: Boolean) -> Unit? = { _, _ -> }
    private var onStartCallback: (animation: Animator?, isReverse: Boolean) -> Unit? = { _, _ -> }

    internal abstract fun setReverse(isReverse: Boolean)
    abstract fun getAnimator(): Animator

    fun setLegacyReverse(isLegacy: Boolean) {
        if (!this.isLegacy) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.isLegacy = isLegacy
            } else {
                throw IllegalStateException("Your version of sdk is lover then 26 API")
            }
        } else {
            this.isLegacy = isLegacy
        }
    }

    protected fun prepare() {
        initInternalCallback()
        applyListeners(getAnimator())
    }

    fun start() {
        isReverse = false

        delayedMainThread(Millis(10)) {
            play()
        }
    }

    private fun play() {
        if (isRunning()) {
            stop()
        }

        prepare()

        getAnimator().apply {
            start()
        }
    }

    private fun applyListeners(animator: Animator) {
        animator.apply {
            removeAllListeners()

            listenerList.forEach { listener ->
                addListener(listener)
            }
        }
    }

    fun stop() {
        getAnimator().cancel()
    }

    fun isRunning(): Boolean {
        return getAnimator().isRunning
    }

    fun reverse() {
        val animator = getAnimator()
        animator.log()
        isReverse = true

        if (!isLegacy) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    if (animator is AnimatorSet) {
                        animator.reverse()
                    } else if (animator is ValueAnimator) {
                        animator.reverse()
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }

                return
            } else throw IllegalStateException("Wtf?")
        } else {
            play()
        }
    }

    fun addListener(listener: Animator.AnimatorListener) {
        listenerList.add(listener)
    }

    fun removeListener(listener: Animator.AnimatorListener) {
        listenerList.remove(listener)
        getAnimator().removeListener(listener)
    }

    fun setOnEndCallback(onEnd: (animation: Animator?, isReverse: Boolean) -> Unit) {
        this.onEndCallback = onEnd
    }

    fun setOnStartCallback(onStart: (animation: Animator?, isReverse: Boolean) -> Unit) {
        this.onStartCallback = onStart
    }

    fun setOnRepeatCallback(onRepeat: (animation: Animator?, isReverse: Boolean) -> Unit) {
        this.onRepeatCallback = onRepeat
    }

    fun setOnCancelCallback(onCancel: (animation: Animator?, isReverse: Boolean) -> Unit) {
        this.onCancelCallback = onCancel
    }

    fun removeAllListeners() {
        listenerList.clear()
        getAnimator().removeAllListeners()
    }

    private fun initInternalCallback() {
        if (internalCallback == null) {
            internalCallback = object: AnimatorListenerAdapter() {
                override fun onAnimationRepeat(animation: Animator?) {
                    onRepeatCallback.invoke(animation, isReverse)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    onEndCallback.invoke(animation, isReverse)
                }

                override fun onAnimationCancel(animation: Animator?) {
                    onCancelCallback.invoke(animation, isReverse)
                }

                override fun onAnimationStart(animation: Animator?) {
                    onStartCallback.invoke(animation, isReverse)
                }

            }

            addListener(internalCallback!!)
        }
    }

    fun removeAllCallbacks() {
        onEndCallback = { _, _ -> }
        onRepeatCallback  = { _, _ -> }
        onCancelCallback = { _, _ -> }
        onStartCallback = { _, _ -> }
    }

    fun log(tag: Any, msg: Any) {
        if (isLogging) {
            Logger.log(tag, msg)
        }
    }
}