package com.merseyside.utils.view.ext

import android.graphics.Point
import kotlin.math.abs

operator fun Point.plus(point: Point): Point {
    return Point(x + point.x, y + point.y)
}

operator fun Point.minus(point: Point): Point {
    return Point(x - point.x, y - point.y)
}

operator fun Point.times(value: Number): Point {
    return Point(x * value.toInt(), y * value.toInt())
}

fun Point.isEmpty(): Boolean {
    return x == 0 && y == 0
}

fun Point.isNotEmpty() = !isEmpty()

fun Pair<Int, Int>.toPoint(): Point {
    return Point(first, second)
}

fun Point.abs() = Point(abs(x), abs(y))

fun Point.max(): Int {
    return kotlin.math.max(x, y)
}

fun Point(value: Number): Point {
    return Point(value.toInt(), value.toInt())
}

fun Point.inverse(): Point {
    return this * -1
}