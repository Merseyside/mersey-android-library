package com.merseyside.merseyLib

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Build
import com.merseyside.merseyLib.utils.Logger

abstract class BaseAnimator {

    var isLogging = false

    private var isLegacy = false
    private var isReverse = false

    set(value) {
        field = value

        if (isLegacy) {
            setReverse(value)
        }
    }

    private val listenerList: MutableList<Animator.AnimatorListener> by lazy { ArrayList<Animator.AnimatorListener>() }

    fun getListeners(): List<Animator.AnimatorListener> = listenerList

    init {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            isLegacy = true
        }
    }

    private var internalCallback: Animator.AnimatorListener? = null

    private var onEndCallback: (animation: Animator?, isReverse: Boolean) -> Unit? = { _, _ -> }
    private var onRepeatCallback: (animation: Animator?, isReverse: Boolean) -> Unit? = { _, _ -> }
    private var onCancelCallback: (animation: Animator?, isReverse: Boolean) -> Unit? = { _, _ -> }
    private var onStartCallback: (animation: Animator?, isReverse: Boolean) -> Unit? = { _, _ -> }

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

    fun start() {
        isReverse = false
        play()
    }

    private fun play() {

        if (isRunning()) {
            stop()
        }

        initInternalCallback()

        getAnimator().apply {
            applyListeners(this)
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

    internal abstract fun setReverse(isReverse: Boolean)

    fun reverse() {
        val animator = getAnimator()
        isReverse = true

        if (!isLegacy) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    if (animator is AnimatorSet) {
                        animator.reverse()
                    } else if (animator is ValueAnimator) {
                        animator.reverse()
                    }
                } catch (e: IllegalStateException) {}

                return
            } else throw IllegalStateException("Wtf?")
        } else {
            play()
        }
    }

    abstract fun getAnimator(): Animator

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
            internalCallback = object: Animator.AnimatorListener {
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