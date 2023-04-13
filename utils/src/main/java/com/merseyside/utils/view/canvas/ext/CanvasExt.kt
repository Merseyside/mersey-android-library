package com.merseyside.utils.view.canvas.ext

import android.graphics.*
import android.view.View
import com.merseyside.utils.view.canvas.Background
import com.merseyside.utils.view.canvas.CircleCorners
import com.merseyside.utils.view.canvas.HorizontalAlign
import com.merseyside.utils.view.canvas.VerticalAlign
import com.merseyside.utils.view.ext.insetNewRect

fun Canvas.drawLine(startX: Float, startY: Float, stopX: Float, stopY: Float, paint: Paint) {
    drawLine(startX, startY, stopX, stopY, paint)
}

fun Canvas.drawLine(from: Point, to: Point, paint: Paint) {
    drawLine(from.x.toFloat(), from.y.toFloat(), to.x.toFloat(), to.y.toFloat(), paint)
}

fun Canvas.drawLine(from: Pair<Number, Number>, to: Pair<Number, Number>, paint: Paint) {
    drawLine(
        from.first.toFloat(),
        from.second.toFloat(),
        to.first.toFloat(),
        to.second.toFloat(),
        paint
    )
}

fun Canvas.drawXLine(x: Number, fromY: Number, toY: Number, paint: Paint) {
    drawLine(x.toFloat(), fromY.toFloat(), x.toFloat(), toY.toFloat(), paint)
}

fun Canvas.drawYLine(y: Number, fromX: Number, toX: Number, paint: Paint) {
    drawLine(fromX.toFloat(), y.toFloat(), toX.toFloat(), y.toFloat(), paint)
}

fun Canvas.drawTextOnBaseline(
    text: String,
    xBaseline: Number,
    yBaseline: Number,
    horizontalAlign: HorizontalAlign = HorizontalAlign.LEFT,
    verticalAlign: VerticalAlign = VerticalAlign.TOP,
    paint: Paint,
    background: Background? = null
) {
    paint.textAlign = Paint.Align.RIGHT

    val xBase = xBaseline.toFloat()
    val yBase = yBaseline.toFloat()

    val textWidth = paint.measureText(text)
    val textHeight = paint.textSize
        .also { if (it == 0F) throw IllegalArgumentException("Text size not set!") }

    val textWidthHalf = textWidth / 2
    val textHeightHalf = textHeight / 2

    val textXCoord = when (horizontalAlign) {
        HorizontalAlign.LEFT -> xBase
        HorizontalAlign.CENTER -> xBase + textWidthHalf
        HorizontalAlign.RIGHT -> xBase + textWidth
    }

    val textYCoord = when (verticalAlign) {
        VerticalAlign.TOP -> yBase
        VerticalAlign.CENTER -> yBase + textHeightHalf
        VerticalAlign.BOTTOM -> yBase + textHeight
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

    drawText(text, textXCoord, textYCoord - textGap, paint)
}

fun Canvas.drawCircle(point: Point, radius: Float, paint: Paint) {
    drawCircle(point.x.toFloat(), point.y.toFloat(), radius, paint)
}

fun Canvas.drawCircle(rect: Rect, radius: Float, paint: Paint) {
    drawCircle(rect.width() / 2F, rect.height() / 2F, radius, paint)
}

fun Canvas.drawTextCenter(rect: Rect, paint: Paint, text: String) {
    val cHeight = rect.height()
    val cWidth = rect.width()
    paint.textAlign = Paint.Align.LEFT
    val bounds = Rect().apply {
        paint.getTextBounds(text, 0, text.length, this)
    }
    val x = rect.left + cWidth / 2f - bounds.width() / 2f - bounds.left
    val y = rect.top + cHeight / 2f + bounds.height() / 2f - bounds.bottom
    drawText(text, x, y, paint)
}

context(View)
fun Canvas.drawRoundRectPreventClip(rect: RectF, rx: Float, ry: Float, paint: Paint) {
    if (paint.style != Paint.Style.FILL) {

        if (left == 0 || top == 0) {
            val halfStrokeWidth = paint.strokeWidth / 2
            val insetRect = rect.insetNewRect(halfStrokeWidth, halfStrokeWidth)
            drawRoundRect(insetRect, rx, ry, paint)

            return
        }
    }

    drawRoundRect(rect, rx, ry, paint)
}

private const val textGap = 5.5f