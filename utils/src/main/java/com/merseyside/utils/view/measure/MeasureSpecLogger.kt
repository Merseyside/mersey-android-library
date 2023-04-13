package com.merseyside.utils.view.measure

import android.view.View.MeasureSpec
import com.merseyside.merseyLib.kotlin.logger.Logger

fun logMeasureSpec(measureSpec: Int, prefix: String = "") {
    val mode = when(MeasureSpec.getMode(measureSpec)) {
        MeasureSpec.AT_MOST -> "AT_MOST"
        MeasureSpec.EXACTLY -> "EXACTLY"
        else -> "UNSPECIFIED"
    }
    val size = MeasureSpec.getSize(measureSpec)

    Logger.log(tag = "MeasureSpec", "$prefix mode = $mode size = $size")
}