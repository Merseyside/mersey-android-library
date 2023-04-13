package com.merseyside.utils.view.ext

import android.graphics.*
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.utils.Quad
import com.merseyside.utils.ext.toFloat
import com.merseyside.utils.view.canvas.Background
import com.merseyside.utils.view.canvas.CircleCorners
import com.merseyside.utils.view.canvas.HorizontalAlign
import com.merseyside.utils.view.canvas.VerticalAlign
import kotlin.math.max
import kotlin.math.min

fun Rect.toQuad(): Quad<Int, Int, Int, Int> {
    return Quad(left, top, right, bottom)
}

fun Rect.set(point: Point) {
    set(point.x, point.y, point.x, point.y)
}

fun Rect.set(leftTop: Point, rightBottom: Point) {
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

context(Canvas)
fun Rect.drawTextCenter(paint: Paint, text: String) {
    val cHeight = height()
    val cWidth = width()
    paint.textAlign = Paint.Align.LEFT
    val bounds = Rect().apply {
        paint.getTextBounds(text, 0, text.length, this)
    }
    val x = left + cWidth / 2f - bounds.width() / 2f - bounds.left
    val y = top + cHeight / 2f + bounds.height() / 2f - bounds.bottom
    drawText(text, x, y, paint)
}

context(Canvas)
fun Rect.drawTextOnBaseline(
    text: String,
    horizontalAlign: HorizontalAlign = HorizontalAlign.LEFT,
    verticalAlign: VerticalAlign = VerticalAlign.TOP,
    paint: Paint,
    background: Background? = null
) {

    val savedAlign = paint.textAlign
    paint.textAlign = Paint.Align.LEFT

    val (rectLeft, rectTop, rectRight, rectBottom) = toQuad().toFloat()

    val textBounds = Rect().apply {
        paint.getTextBounds(text, 0, text.length, this)
    }

    val textWidth = paint.getTextWidth(text)
    val textHeight = paint.getTextHeight(text)

    val textXCoord = when (horizontalAlign) {
        HorizontalAlign.LEFT -> rectLeft
        HorizontalAlign.CENTER -> rectLeft + (width() / 2F) - textBounds.width() / 2F - textBounds.left
        HorizontalAlign.RIGHT -> rectRight
    }

    val textYCoord = when (verticalAlign) {
        VerticalAlign.TOP -> rectTop + textHeight
        VerticalAlign.CENTER -> rectTop + (height() / 2F) + textBounds.height() / 2F - textBounds.bottom
        VerticalAlign.BOTTOM -> rectBottom
    }

    background?.let { background ->
        val rect = RectF(
            textXCoord - textWidth - background.margins.left,
            textYCoord - textHeight - background.margins.top,
            textXCoord + background.margins.right,
            textYCoord + background.margins.bottom
        )

        background.cornerRadius?.let { radius ->
            val newRadius = if (radius is CircleCorners) {
                radius.getCornerRadius(rect)
            } else {
                radius
            }
            drawRoundRect(
                rect,
                newRadius.xRadius.toFloat(),
                newRadius.yRadius.toFloat(),
                background.paint
            )
        } ?: drawRect(rect, background.paint)
    }

    drawText(text, textXCoord, textYCoord/* - textGap */, paint)
    paint.textAlign = savedAlign
}

fun Rect.getCenterPoint(): Point {
    return Point(centerX(), centerY())
}

fun Rect.isSquare(): Boolean {
    return width() == height()
}

/**
 * Insets rect to square. It means rectangle size will be equals to the smallest side's size
 */
fun Rect.insetToSquare() {
    if (!isSquare()) {
        val minSide = min(width(), height())

        val centerPoint = getCenterPoint()
        set(centerPoint)
        expand(minSide / 2, minSide / 2)
    }
}

/**
 * Expands rect to square. It means rectangle size will be equals to the largest side's size
 */
fun Rect.expandToSquare() {
    if (!isSquare()) {
        val maxSide = max(width(), height())

        val centerPoint = getCenterPoint()
        set(centerPoint)
        expand(maxSide / 2, maxSide / 2)
    }
}



fun Rect.logRect(tag: String = Logger.TAG, prefix: String? = ""): Rect {
    return this.also { Logger.log(tag, "$prefix left = $left top = $top\nright = $right bottom = $bottom") }
}

fun RectF.logRect(tag: String = Logger.TAG, prefix: String? = ""): RectF {
    return this.also { Logger.log(tag, "$prefix left = $left top = $top\nright = $right bottom = $bottom") }
}



