package com.merseyside.archy.presentation.view.localeViews

import android.content.Context
import android.view.View
import android.view.ViewGroup

interface ILocaleManager {

    fun updateLocale(root: View? = getRootView(), context: Context? = getRootView()?.context) {

        if (root != null && context != null) {

            if (root is ViewGroup) {
                for (i in 0 until root.childCount) {
                    updateLocale(root.getChildAt(i), context)
                }
            }

            if (root is ILocaleView) {
                root.updateLocale(context)
            }
        }
    }

    fun getRootView(): View?
}