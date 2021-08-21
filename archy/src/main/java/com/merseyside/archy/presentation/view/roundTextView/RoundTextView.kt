package com.merseyside.archy.presentation.view.roundTextView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.merseyside.archy.R
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.delegate.dimension
import com.merseyside.utils.ext.getColorFromAttr
import com.merseyside.utils.ext.isZero
import com.merseyside.utils.ext.log
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

    private lateinit var rect: RectF

    private val attrs = AttributeHelper(this, attributeSet)

    private var cornerRadius: Float by attrs.dimension(resName = "cornerRadius", defaultValue = 0F)

    private val paint = Paint().apply {
        color = attrs.getColor(
            resName = "backgroundColor",
            defValue = getColorFromAttr(R.attr.backgroundColor)
        )
    }

    init {
        cornerRadius.log(prefix = "radius")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (cornerRadius.isZero()) {
            cornerRadius = min(measuredWidth, measuredHeight).toFloat() / 2
        }

        rect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        super.onDraw(canvas)
    }
}