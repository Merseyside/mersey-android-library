package com.merseyside.utils.view.globalPositioning

import android.graphics.Point
import android.graphics.Rect
import android.view.View
import com.merseyside.utils.view.ext.Rect
import com.merseyside.utils.view.ext.getIntersectedRect
import com.merseyside.utils.view.ext.minus
import com.merseyside.utils.view.ext.plus

fun globalXToScreenCoords(
    coord: Int,
    screenWidth: Int,
    scrollPosition: Point
): Int? {
    return if (coord in scrollPosition.x..scrollPosition.x + screenWidth) {
        coord - scrollPosition.x
    } else null
}

fun globalYToScreenCoords(
    coord: Int,
    screenHeight: Int,
    scrollPosition: Point
): Int? {
    return if (coord in scrollPosition.y..scrollPosition.y + screenHeight) {
        coord - scrollPosition.y
    } else null
}

fun globalPointToScreenPoint(
    point: Point,
    screenWidth: Int,
    screenHeight: Int,
    scrollPosition: Point
): Point? {
    return Point(
        globalXToScreenCoords(point.x, screenWidth, scrollPosition) ?: return null,
        globalYToScreenCoords(point.y, screenHeight, scrollPosition) ?: return null
    )
}

fun screenCoordToGlobalCoord(
    screenCoord: Point,
    scrollPosition: Point
): Point {
    return scrollPosition + screenCoord
}

fun getScreenCoordByScrollPosition(
    screenCoord: Point,
    scrollPosition: Point
): Point {
    return screenCoord - scrollPosition
}

fun getGlobalScreenRect(
    screenWidth: Int,
    screenHeight: Int,
    scrollPosition: Point
): Rect {
    return Rect(
        scrollPosition.x,
        scrollPosition.y,
        scrollPosition.x + screenWidth,
        scrollPosition.y + screenHeight
    )
}

fun globalRectToVisibleRect(
    globalRect: Rect,
    screenWidth: Int,
    screenHeight: Int,
    scrollPosition: Point,
    screenVisibleRect: Rect? = null
): Rect? {
    return with(globalRect) {

        val globalScreenRect = getGlobalScreenRect(screenWidth, screenHeight, scrollPosition)
        val intersectedRect = Rect(Point(left, top), Point(right, bottom)).run {
            getIntersectedRect(globalScreenRect)
        }

        intersectedRect?.let { intersected ->
            val leftTop = globalPointToScreenPoint(
                Point(intersected.left, intersected.top),
                screenWidth,
                screenHeight,
                scrollPosition
            )
            val rightBottom = globalPointToScreenPoint(
                Point(intersected.right, intersected.bottom),
                screenWidth,
                screenHeight,
                scrollPosition
            )

            if (leftTop == null || rightBottom == null) null
            else {
                val rect = Rect(leftTop, rightBottom)
                if (screenVisibleRect != null) {
                    screenVisibleRect.getIntersectedRect(rect)
                } else {
                    rect
                }
            }
        }
    }
}

fun getRelativeToParentGlobalRect(view: View, rect: Rect) {
    val x = view.x.toInt()
    val y = view.y.toInt()

    rect.left = x
    rect.right = x + view.width
    rect.top = y
    rect.bottom = y + view.height
}