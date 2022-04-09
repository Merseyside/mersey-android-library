package com.merseyside.utils.view.globalPositioning

import android.graphics.Point
import android.view.View
import com.merseyside.utils.view.ViewBaseline
import com.merseyside.utils.view.ext.setCoordPoint

fun View.setViewByGlobalPosition(
    globalPosition: Point,
    scrollPosition: Point,
    baseline: Int = ViewBaseline.UNSPECIFIED
) {
    setCoordPoint(getScreenCoordByScrollPosition(globalPosition, scrollPosition), baseline)
}