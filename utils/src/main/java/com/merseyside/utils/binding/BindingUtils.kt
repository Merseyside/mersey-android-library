package com.merseyside.utils.binding

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.merseyside.merseyLib.kotlin.extensions.flatWithAnd
import com.merseyside.merseyLib.kotlin.extensions.flatWithOr
import com.merseyside.merseyLib.kotlin.logger.logMsg
import com.merseyside.utils.view.ext.getActivity

/**
 * BU for BindingUtils, truncated for the sake of shortening in xml files
 */
object BU {

    @JvmStatic
    fun isGone(view: View, id: Int): Boolean {
        val targetView = getViewById(view, id)
        return targetView.isGone
    }

    @JvmStatic
    fun isVisible(view: View, id: Int): Boolean {
        val targetView = getViewById(view, id)
        return targetView.isVisible
    }

    @JvmStatic
    fun setGone(view: View, id: Int, isGone: Boolean) {
        val viewWithState = getViewById(view, id)
        viewWithState.isGone = isGone
    }

    @JvmStatic
    fun setVisible(view: View, id: Int, isVisible: Boolean) {
        val viewWithState = getViewById(view, id)
        viewWithState.isVisible = isVisible
    }

    @JvmStatic
    fun or(vararg booleans: Boolean): Boolean {
        return booleans.toList().flatWithOr()
    }

    @JvmStatic
    fun and(vararg booleans: Boolean): Boolean {
        return booleans.toList().flatWithAnd()
    }

    @JvmStatic
    fun less(first: Number, second: Number): Boolean {
        return first.toFloat() < second.toFloat()
    }

    @JvmStatic
    fun more(first: Number, second: Number): Boolean {
        return first.toFloat() > second.toFloat()
    }

    @JvmStatic
    fun isNull(obj: Any?) = obj == null

    @JvmStatic
    fun isNotNull(obj: Any?) = !isNull(obj)

    @JvmStatic
    fun isEmpty(list: List<*>) = list.isEmpty()

    @JvmStatic
    fun isNotEmpty(list: List<*>) = list.isNotEmpty()

    @JvmStatic
    fun getTag(view: View, id: Int): Any? {
        return getViewById(view, id).tag
    }

    @JvmStatic
    fun log(tag: String = "BindingUtils", msg: Any = "empty message") {
        logMsg(tag, msg)
    }

    private fun getViewById(view: View, id: Int): View {
        val activityRootView = view.getActivity().findViewById<ViewGroup>(android.R.id.content)
        return activityRootView.findViewById(id)
    }
}