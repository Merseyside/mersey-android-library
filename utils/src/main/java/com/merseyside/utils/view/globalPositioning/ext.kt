package com.merseyside.utils.view.globalPositioning

import android.graphics.Point
import android.view.View
import com.merseyside.utils.ext.setCoordPoint
import com.merseyside.utils.view.ViewBaseline

fun View.setViewByGlobalPosition(
    globalPosition: Point,
    scrollPosition: Point,
    baseline: Int = ViewBaseline.UNSPECIFIED
) {
    setCoordPoint(getScreenCoordByScrollPosition(globalPosition, scrollPosition), baseline)
}