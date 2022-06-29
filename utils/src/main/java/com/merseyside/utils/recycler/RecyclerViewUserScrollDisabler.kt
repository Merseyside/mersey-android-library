package com.merseyside.utils.recycler

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * Disables scrolling by user.
 * Add in addOnItemTouchListener()
 */
class RecyclerViewUserScrollDisabler: RecyclerView.OnItemTouchListener {
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent) = true

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}