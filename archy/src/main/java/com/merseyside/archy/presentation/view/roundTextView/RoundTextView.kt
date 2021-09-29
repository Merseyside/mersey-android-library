package com.merseyside.archy.presentation.view.roundTextView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.merseyside.archy.R
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.delegate.dimension
import com.merseyside.utils.ext.getColorFromAttr
import com.merseyside.utils.ext.isZero
import kotlin.math.min

class RoundTextView(
    context: Context,
    attributeSet: AttributeSet,
    style: Int
) : AppCompatTextView(context, attributeSet, style) {

    constructor(
        context: Context,
        attributeSet: AttributeSet
    ) : this(context, attributeSet, 0)

    private var rect: RectF = RectF().apply {
        left = 0F
        top = 0F
    }

    private var strokeRect = RectF()

    private val attrs = AttributeHelper(this, attributeSet)

    private var cornerRadius: Float by attrs.dimension(resName = "cornerRadius", defaultValue = 0F)
    private var strokeWidth: Float by attrs.dimension(defaultValue = 0F)

    private val paint = Paint().apply {
        color = attrs.getColor(
            resName = "fillColor",
            defValue = ContextCompat.getColor(context, android.R.color.transparent)
        )
    }

    private val strokePaint = Paint().apply {
        color = attrs.getColor(
            resName = "strokeColor",
            defValue = getColorFromAttr(R.attr.colorAccent)
        )
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
        super.onDraw(canvas)
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        canvas.drawRoundRect(strokeRect, cornerRadius, cornerRadius, strokePaint)
    }
}