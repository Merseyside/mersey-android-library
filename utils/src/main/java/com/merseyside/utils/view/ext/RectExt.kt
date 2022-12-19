package com.merseyside.utils.view.ext

import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF

fun Rect.set(leftTop: Point = Point(left, top), rightBottom: Point = Point(right, bottom)) {
    set(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y)
}

fun Rect(leftTop: Point, rightBottom: Point): Rect {
    return Rect(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y)
}

fun RectF(leftTop: Point, rightBottom: Point): RectF {
    return RectF(leftTop.x.toFloat(), leftTop.y.toFloat(), rightBottom.x.toFloat(), rightBottom.y.toFloat())
}

fun Rect.leftTop(): Point {
    return Point(left, top)
}

fun Rect.rightBottom(): Point {
    return Point(right, bottom)
}

fun Rect.insetNewRect(dx: Int, dy: Int): Rect {
    return Rect(this).apply {
        inset(dx, dy)
    }
}

fun RectF.insetNewRect(dx: Float, dy: Float): RectF {
    return RectF(this).apply {
        inset(dx, dy)
    }
}

fun Rect.intersect(point: Point): Boolean {
    return intersect(Rect(point, point))
}

fun Rect.contains(point: Point): Boolean {
    return contains(point.x, point.y)
}

fun Rect.expand(dx: Int, dy: Int) {
    left -= dx
    top -= dy
    right += dx
    bottom += dy
}

fun Rect.getIntersectedRect(
    anotherRect: Rect
): Rect? {
    val intersectedRect = Rect()
    return if (intersectedRect.setIntersect(this, anotherRect)) {
        intersectedRect
    } else null
}

