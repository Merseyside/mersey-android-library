package com.merseyside.archy.presentation.view.merseyRecyclerView

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.absoluteValue

class FixedScrollRecyclerView(context: Context, attrSet: AttributeSet? = null)
    : MerseyRecyclerView(context, attrSet) {

    private var verticalScrollOffset = AtomicInteger(0)

    init {

        addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            val y = oldBottom - bottom
            if (y.absoluteValue > 0) {
                post {
                    if (y > 0 || verticalScrollOffset.get().absoluteValue >= y.absoluteValue) {
                        scrollBy(0, y)
                    } else {
                        scrollBy(0, verticalScrollOffset.get())
                    }
                }
            }
        }

        addOnScrollListener(object : OnScrollListener() {
            var state = AtomicInteger(SCROLL_STATE_IDLE)

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                state.compareAndSet(SCROLL_STATE_IDLE, newState)
                when (newState) {
                    SCROLL_STATE_IDLE -> {
                        if (!state.compareAndSet(SCROLL_STATE_SETTLING, newState)) {
                            state.compareAndSet(SCROLL_STATE_DRAGGING, newState)
                        }
                    }
                    SCROLL_STATE_DRAGGING -> {
                        state.compareAndSet(SCROLL_STATE_IDLE, newState)
                    }
                    SCROLL_STATE_SETTLING -> {
                        state.compareAndSet(SCROLL_STATE_DRAGGING, newState)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (state.get() != SCROLL_STATE_IDLE) {
                    verticalScrollOffset.getAndAdd(dy)
                }
            }
        })
    }
}