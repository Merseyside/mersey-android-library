package com.merseyside.utils.view.gesture

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Sometimes ACTION_DOWN is skipped. That's why non nullable params in gesture listener are null and crash occurs.
 * Following class checks is DOWN action received.
 * issuetracker.google.com/issues/238920463
 */

class SafeGestureDetector(context: Context, listener: OnGestureListener) :
    GestureDetector(context, listener) {

    private var hasDownState: Boolean = false

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) hasDownState = true

        return if (hasDownState) {
            super.onTouchEvent(ev).also {
                if (ev.action == MotionEvent.ACTION_UP) hasDownState = false
            }
        } else false
    }

}