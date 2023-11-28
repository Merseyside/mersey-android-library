package com.merseyside.archy.presentation.view.safeViewPager2Wrapper

import androidx.viewpager2.widget.ViewPager2
import com.merseyside.utils.view.touchInterceptor.TouchEventInterceptor

class SafeViewPager2(
    val viewPager: ViewPager2,
    private var listener: OnPageChangedListener?
) {

    private var enableTouches: Boolean = true
    private var areTouchesDisabled: Boolean = false
    private lateinit var interceptorView: TouchEventInterceptor

    private var savedPosition = -1

    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (savedPosition == -1) {
                    savedPosition = position
                    listener?.onPageChanged(savedPosition)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                handleState(state)

                if (state == ViewPager2.SCROLL_STATE_IDLE && savedPosition != -1) {
                    if (viewPager.currentItem != savedPosition) {
                        savedPosition = viewPager.currentItem
                        listener?.onPageChanged(savedPosition)
                    }
                }
            }
        })
    }

    private fun disableTouchesWhenScrolling(
        interceptor: TouchEventInterceptor? = null
    ) {
        enableTouches = false

        val parent = viewPager.parent
        interceptorView = interceptor
            ?: if (parent !is TouchEventInterceptor) throw IllegalStateException(
                "Parent view is not TouchEventInterceptor"
            ) else parent
    }

    private fun handleState(state: Int) {
        if (!enableTouches) {
            if (state != ViewPager2.SCROLL_STATE_IDLE && !areTouchesDisabled) {
                areTouchesDisabled = true
                interceptorView.addInterceptor { _ -> true }
            }
            if (state == ViewPager2.SCROLL_STATE_IDLE && areTouchesDisabled) {
                interceptorView.removeAllInterceptors()
                areTouchesDisabled = false
                enableTouches = true
            }
        }
    }

    fun setCurrentItem(
        position: Int,
        isSmooth: Boolean = true,
        disableTouches: Boolean = isSmooth,
        interceptor: TouchEventInterceptor? = null
    ) {
        if (disableTouches) {
            disableTouchesWhenScrolling(interceptor)
        }

        viewPager.setCurrentItem(position, isSmooth)
    }

    fun interface OnPageChangedListener {
        fun onPageChanged(position: Int)
    }
}