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

        setReverse(value)
    }

    private val listenerList: MutableList<Animator.AnimatorListener> by lazy { ArrayList<Animator.AnimatorListener>() }

    fun getListeners(): List<Animator.AnimatorListener> = listenerList

    init {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            isLegacy = true
        }
    }

    private var internalCallback: Animator.AnimatorListener? = null

    private var onEndCallback: (animation: Animator?) -> Unit? = {}
    private var onRepeatCallback: (animation: Animator?) -> Unit? = {}
    private var onCancelCallback: (animation: Animator?) -> Unit? = {}
    private var onStartCallback: (animation: Animator?) -> Unit? = {}

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
        if (isLegacy) {
            isReverse = false
        }

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

        if (!isLegacy) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if (animator is AnimatorSet) {
                    animator.reverse()
                } else if (animator is ValueAnimator) {
                    animator.reverse()
                }

                return
            } else throw IllegalStateException("Wtf?")
        } else {
            isReverse = true

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

    fun setOnEndCallback(onEnd: (animation: Animator?) -> Unit) {
        this.onEndCallback = onEnd
    }

    fun setOnStartCallback(onStart: (animation: Animator?) -> Unit) {
        this.onStartCallback = onStart
    }

    fun setOnRepeatCallback(onRepeat: (animation: Animator?) -> Unit) {
        this.onRepeatCallback = onRepeat
    }

    fun setOnCancelCallback(onCancel: (animation: Animator?) -> Unit) {
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
                    onRepeatCallback.invoke(animation)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    onEndCallback.invoke(animation)
                }

                override fun onAnimationCancel(animation: Animator?) {
                    onCancelCallback.invoke(animation)
                }

                override fun onAnimationStart(animation: Animator?) {
                    onStartCallback.invoke(animation)
                }

            }

            addListener(internalCallback!!)
        }
    }

    fun removeAllCallbacks() {
        onEndCallback = {}
        onRepeatCallback  = {}
        onCancelCallback = {}
        onStartCallback = {}
    }

    fun log(tag: Any, msg: Any) {
        if (isLogging) {
            Logger.log(tag, msg)
        }
    }

}