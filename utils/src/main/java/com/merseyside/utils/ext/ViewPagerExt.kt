package com.merseyside.utils.ext

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.merseyside.utils.view.touchInterceptor.TouchEventInterceptor

fun ViewPager2.setCurrentItem(
    position: Int,
    smoothScroll: Boolean = true,
    disableTouchesOnScroll: Boolean = smoothScroll,
    interceptor: TouchEventInterceptor? = null
) {
    if (disableTouchesOnScroll) {

        val interceptorView = interceptor
            ?: if (parent !is TouchEventInterceptor) throw IllegalStateException(
                "Parent view is not TouchEventInterceptor"
            ) else parent as TouchEventInterceptor

        registerOnPageChangeCallback(object : OnPageChangeCallback() {
            var disabled = false
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state != ViewPager2.SCROLL_STATE_IDLE && !disabled ) {
                    disabled = true
                    interceptorView.addInterceptor { _ -> true }
                }
                if (state == ViewPager2.SCROLL_STATE_IDLE && disabled) {
                    interceptorView.removeAllInterceptors()
                    unregisterOnPageChangeCallback(this)
                }
            }
        })
    }

    setCurrentItem(position, smoothScroll)
}

fun ViewPager2.recyclerView(): RecyclerView {
    return getChildAt(0) as RecyclerView
}