package com.merseyside.utils.view.ext

import android.graphics.Paint

fun Paint.getTextWidth(text: String): Int {
    val bounds = android.graphics.Rect()
    getTextBounds(text, 0, text.length, bounds)
    return bounds.width()
}

fun Paint.getTextHeight(text: String): Int {
    val bounds = android.graphics.Rect()
    getTextBounds(text, 0, text.length, bounds)
    return bounds.height()
}