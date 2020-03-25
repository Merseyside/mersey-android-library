package com.merseyside.merseyLib.utils

import android.app.Activity
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.utils.ext.getColorFromAttr

open class SnackbarManager(private val activity: Activity) {

    private var snackbar: Snackbar? = null

    private val callback = object: Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)

            snackbar = null
        }
    }

    fun isShowing(): Boolean {
        return snackbar != null
    }

    fun showSnackbar(
        message: String,
        actionMsg: String?,
        clickListener: View.OnClickListener?
    ) {
        val backgroundColor = getMsgBackgroundColor()
        val textColor = getMsgTextColor()
        val actionColor = getActionMsgTextColor()

        showSnackbarDefault(message, backgroundColor, textColor, actionColor, actionMsg, clickListener)
    }

    fun showErrorSnackbar(
        message: String,
        actionMsg: String?,
        clickListener: View.OnClickListener?
    ) {
        val backgroundColor = getErrorMsgBackgroundColor()
        val textColor = getErrorMsgTextColor()
        val actionColor = getActionErrorMsgTextColor()

        showSnackbarDefault(message, backgroundColor, textColor, actionColor, actionMsg, clickListener)
    }

    private fun showSnackbarDefault(
        message: String,
        @ColorInt backgroundColor: Int,
        @ColorInt textColor: Int,
        @ColorInt actionColor: Int,
        actionMsg: String?,
        clickListener: View.OnClickListener?
    ) {
        val length = if (!actionMsg.isNullOrEmpty()) {
            Snackbar.LENGTH_INDEFINITE
        } else {
            Snackbar.LENGTH_LONG
        }

        createSnackbar(message, length, backgroundColor, textColor).apply {

            if (!actionMsg.isNullOrEmpty()) {
                var listener = clickListener

                if (listener == null) listener = View.OnClickListener {}

                setAction(actionMsg, listener)
                setActionTextColor(actionColor)
            }

            show()
        }
    }

    private fun createSnackbar(message: String, length: Int, @ColorInt backgroundColor: Int, @ColorInt textColor: Int): Snackbar {

        val viewGroup = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        snackbar = Snackbar.make(viewGroup, message, length)
        val snackbarView = snackbar!!.view

        snackbarView.setBackgroundColor(backgroundColor)

        val snackTextView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
        snackTextView.setTextColor(textColor)

        val font = Typeface.createFromAsset(activity.assets, "fonts/Roboto-Regular.ttf")
        var tv = snackbar!!.view.findViewById<TextView>(R.id.snackbar_text)
        tv.typeface = font
        tv = snackbar!!.view.findViewById(R.id.snackbar_action)
        tv.typeface = font

        setCallback(callback)

        return snackbar!!
    }

    fun dismiss() {
        snackbar?.apply {
            dismiss()
            snackbar = null
        }
    }

    private fun setCallback(callback: Snackbar.Callback) {
        snackbar?.addCallback(callback)
    }

    @ColorInt
    open fun getMsgBackgroundColor(): Int {
        return activity.getColorFromAttr(R.attr.colorPrimary)
    }

    @ColorInt
    open fun getErrorMsgBackgroundColor(): Int {
        return activity.getColorFromAttr(R.attr.colorError)
    }

    @ColorInt
    open fun getMsgTextColor(): Int {
        return activity.getColorFromAttr(R.attr.colorOnPrimary)
    }

    @ColorInt
    open fun getErrorMsgTextColor(): Int {
        return activity.getColorFromAttr(R.attr.colorOnError)
    }

    @ColorInt
    open fun getActionMsgTextColor(): Int {
        return activity.getColorFromAttr(R.attr.colorOnPrimary)
    }

    @ColorInt
    open fun getActionErrorMsgTextColor(): Int {
        return activity.getColorFromAttr(R.attr.colorOnError)
    }
}