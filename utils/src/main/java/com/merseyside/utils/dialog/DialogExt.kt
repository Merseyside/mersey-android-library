package com.merseyside.utils.dialog

import android.app.Dialog
import android.content.res.Resources
import android.view.View
import androidx.annotation.DimenRes
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.merseyside.merseyLib.kotlin.utils.safeLet

fun Dialog.setLayoutSizeRes(@DimenRes widthRes: Int? = null, @DimenRes heightRes: Int? = null) {
    setLayoutSize(
        safeLet(widthRes) { w -> context.resources.getDimensionPixelSize(w) },
        safeLet(heightRes) { h -> context.resources.getDimensionPixelSize(h) }
    )
}

fun Dialog.setWidthPercentage(percentage: Float) {
    val availableWidth = Resources.getSystem().displayMetrics.widthPixels
    setLayoutSize(width = (availableWidth * percentage).toInt())
}

fun Dialog.setHeightPercentage(percentage: Float) {
    val availableHeight = Resources.getSystem().displayMetrics.heightPixels
    setLayoutSize(height = (availableHeight * percentage).toInt())
}

fun Dialog.setLayoutSize(width: Int? = null, height: Int? = null) {

    if (this is BottomSheetDialog) {
        val sheet = findViewById<View>(R.id.design_bottom_sheet)
            ?: throw NullPointerException("Sheet is null!")

        safeLet(width) { w -> sheet.layoutParams.width = w }
        safeLet(height) { h -> sheet.layoutParams.height = h }
    } else {
        window?.apply {
            attributes = attributes.apply {
                if (width != null) this.width = width
                if (height != null) this.height = height
            }
        } ?: throw IllegalStateException("Dialog is null!")
    }
}