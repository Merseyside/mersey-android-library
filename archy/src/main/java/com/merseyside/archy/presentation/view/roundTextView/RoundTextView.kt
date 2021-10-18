package com.merseyside.archy.presentation.view.roundTextView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.ext.isZero
import kotlin.math.min

class RoundTextView(
    context: Context,
    attributeSet: AttributeSet?,
    style: Int
) : AppCompatTextView(context, attributeSet, style) {

    private var cornerRadius: Float = 0F
    private var strokeWidth: Float = 0F
    private var fillColor: Int = 0
    private var strokeColor: Int = 0

    constructor(
        context: Context,
        attributeSet: AttributeSet
    ) : this(context, attributeSet, 0)

    constructor(
        context: Context,
        cornerRadius: Float = 0F,
        strokeWidth: Float = 0F,
        fillColor: Int = 0,
        strokeColor: Int = 0
    ): this(context, null, 0) {
        this.cornerRadius = cornerRadius
        this.strokeColor = strokeColor
        this.strokeWidth = strokeWidth
        this.fillColor = fillColor
    }

    init {
        if (attributeSet != null) {
            AttributeHelper(this, attributeSet).run {
                cornerRadius = getDimension(resName = "cornerRadius", defValue = 0F)
                strokeWidth = getDimension(resName = "strokeWidth", defValue = 0F)
                fillColor = getColor(
                    resName = "fillColor",
                    defValue = ContextCompat.getColor(context, android.R.color.transparent)
                )
                strokeColor = getColor(
                    resName = "strokeColor",
                    defValue = fillColor
                )
            }
        }
    }

    private var rect: RectF = RectF().apply {
        left = 0F
        top = 0F
    }

    private var strokeRect = RectF()

    private val paint = Paint().apply {
        color = fillColor
    }

    private val strokePaint = Paint().apply {
        color = strokeColor
        strokeWidth = this@RoundTextView.strokeWidth
        setStyle(Paint.Style.STROKE)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (cornerRadius.isZero()) {
            cornerRadius = min(measuredWidth, measuredHeight).toFloat() / 2
        }

        rect.apply {
            right = measuredWidth.toFloat()
            bottom = measuredHeight.toFloat()
        }

        strokeRect.apply {
            set(rect)
            inset(strokeWidth / 2, strokeWidth / 2)
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        canvas.drawRoundRect(strokeRect, cornerRadius, cornerRadius, strokePaint)
        super.onDraw(canvas)
    }
}