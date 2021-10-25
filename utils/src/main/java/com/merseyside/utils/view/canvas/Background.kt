package com.merseyside.utils.view.canvas

import android.graphics.Paint

data class Background(
    val paint: Paint,
    val cornerRadius: CornerRadius? = null,
    val margins: Margins = Margins.getEmpty()
)
