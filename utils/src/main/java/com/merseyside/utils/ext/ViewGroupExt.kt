package com.merseyside.utils.ext

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.DimenRes

fun ViewGroup.LayoutParams.setMarginsRes(
    context: Context,
    @DimenRes left: Int? = null,
    @DimenRes top: Int? = null,
    @DimenRes right: Int? = null,
    @DimenRes bottom: Int? = null
) {
    val resources = context.resources
    setMargins(
        left?.let { resources.getDimensionPixelSize(left) },
        top?.let { resources.getDimensionPixelSize(top) },
        right?.let { resources.getDimensionPixelSize(right) },
        bottom?.let { resources.getDimensionPixelSize(bottom) },
    )
}

fun ViewGroup.LayoutParams.setMargins(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    val params = (this as? ViewGroup.MarginLayoutParams)
    params?.setMargins(
        left ?: params.leftMargin,
        top ?: params.topMargin,
        right ?: params.rightMargin,
        bottom ?: params.bottomMargin
    )
}