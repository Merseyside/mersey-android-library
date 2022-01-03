package com.merseyside.utils.ext

import androidx.cardview.widget.CardView

fun CardView.setContentPadding(padding: Int) {
    setContentPadding(padding, padding, padding, padding)
}