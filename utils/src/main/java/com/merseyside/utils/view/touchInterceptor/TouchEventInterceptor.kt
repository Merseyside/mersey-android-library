package com.merseyside.utils.view.touchInterceptor

import android.view.MotionEvent

interface TouchEventInterceptor {

    val interceptors: MutableList<OnTouchInterceptor>

    fun intercept(ev: MotionEvent?): Boolean {
        val foundInterceptor = interceptors
            .find { interceptor -> interceptor.onInterceptTouchEvent(ev) }

        return foundInterceptor != null
    }

    fun addInterceptor(interceptor: OnTouchInterceptor) {
        interceptors.add(interceptor)
    }

    fun removeInterceptor(interceptor: OnTouchInterceptor) {
        interceptors.remove(interceptor)
    }

    fun removeAllInterceptors() {
        interceptors.clear()
    }

    fun interface OnTouchInterceptor {

        fun onInterceptTouchEvent(ev: MotionEvent?): Boolean
    }
}

