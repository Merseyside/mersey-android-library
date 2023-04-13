package com.merseyside.archy.presentation.view.roundTextView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.merseyside.archy.R
import com.merseyside.merseyLib.kotlin.extensions.isZero
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.attributes.color
import com.merseyside.utils.attributes.dimension
import kotlin.math.min

class RoundTextView(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int
) : AppCompatTextView(context, attributeSet, defStyleAttr) {

    private val attrs = AttributeHelper(
        context,
        attributeSet,
        R.styleable.RoundTextView,
        defStyleAttr,
    )

    var cornerRadius by attrs.dimension(
        defaultValue = 0f,
        resId = R.styleable.RoundTextView_roundCornerRadius
    )
    var strokeWidth by attrs.dimension(
        defaultValue = 0f,
        resId = R.styleable.RoundTextView_roundStrokeWidth
    )
    var fillColor by attrs.color(
        defaultValue = ContextCompat.getColor(
            context,
            android.R.color.transparent
        ), resId = R.styleable.RoundTextView_roundFillColor
    )
    var strokeColor by attrs.color(
        defaultValue = ContextCompat.getColor(
            context,
            android.R.color.transparent
        ), resId = R.styleable.RoundTextView_roundStrokeColor
    )

    constructor(
        context: Context,
        attributeSet: AttributeSet
    ) : this(context, attributeSet, R.attr.roundTextViewStyle)

    constructor(
        context: Context,
        cornerRadius: Float = 0F,
        strokeWidth: Float = 0F,
        fillColor: Int = 0,
        strokeColor: Int = 0
    ) : this(context, null, 0) {
        this.cornerRadius = cornerRadius
        this.strokeColor = strokeColor
        this.strokeWidth = strokeWidth
        this.fillColor = fillColor
    }

    private var rect: RectF = RectF().apply {
        left = 0F
        top = 0F
    }

    private var strokeRect = RectF()

    private val paint by lazy {
        Paint().apply {
            color = fillColor
        }
    }

    private val strokePaint by lazy {
        Paint().apply {
            color = strokeColor
            strokeWidth = this@RoundTextView.strokeWidth
            style = Paint.Style.STROKE
        }
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

    @JvmName("setCornerRadiusJvm")
    fun setCornerRadius(radius: Float) {
        this.cornerRadius = radius
        invalidate()
    }

    @JvmName("setStrokeWidthJvm")
    fun setStrokeWidth(width: Float) {
        this.strokeWidth = width
        strokePaint.strokeWidth = width
        invalidate()
    }

    @JvmName("setFillColorJvm")
    fun setFillColor(@ColorInt color: Int) {
        this.fillColor = color
        paint.color = color
        invalidate()
    }

    @JvmName("setStrokeColorJvm")
    fun setStrokeColor(@ColorInt color: Int) {
        this.strokeColor = color
        strokePaint.color = color
        invalidate()
    }
}