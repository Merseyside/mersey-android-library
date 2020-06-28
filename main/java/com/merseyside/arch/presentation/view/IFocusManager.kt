package com.merseyside.archy.presentation.view

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button

interface IFocusManager {

    fun setOnFocusListener(root: View = getRootView(), listener: View.OnTouchListener) {

        if (root is ViewGroup) {
            for (i in 0 until root.childCount) {
                setOnFocusListener(root.getChildAt(i), listener)
            }
        } else {
            root.setOnTouchListener(listener)
        }
    }

    fun clearFocus(vararg excludeViews: View) {

        fun clearFocus(view: View) {
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    clearFocus(view.getChildAt(i))
                }
            } else {
                if (!excludeViews.contains(view) || view is Button) {
                    view.clearFocus()
                }
            }
        }

        clearFocus(getRootView())
    }

    @SuppressLint("ClickableViewAccessibility")
    fun keepOneFocusedView() {
        setOnFocusListener(listener = View.OnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_UP -> {
                    clearFocus(v)
                }
            }

            false
        })
    }

    fun getRootView(): View

    companion object {
        private const val TAG = "IFocusManager"
    }
}