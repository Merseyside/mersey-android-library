package com.merseyside.utils.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.utils.ext.isNotZero
import kotlin.math.min

open class WrapContentLinearLayoutManager : LinearLayoutManager {

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    var measuredWidth: Int = 0
    var measuredHeight: Int = 0

    private var desiredItemSize: Int = 0

    constructor(
        context: Context,
        desiredItemSize: Int,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(
        context, orientation, reverseLayout
    ) {
        this.desiredItemSize = desiredItemSize
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (ignored: IndexOutOfBoundsException) {
        }
    }

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)

        var calculatedWidth = 0
        var calculatedHeight = 0

        var desiredSize = 0

        val intArray = IntArray(2)

        for (i in 0 until itemCount) {
            desiredSize += desiredItemSize
            try {
                measureScrapChild(
                    recycler, i,
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    intArray
                )
                if (orientation == HORIZONTAL) {
                    calculatedWidth += intArray[0]
                    if (i == 0) {
                        calculatedHeight = intArray[1]
                    }
                } else {
                    calculatedHeight += intArray[1]
                    if (i == 0) {
                        calculatedWidth = intArray[0]
                    }
                }
            } catch (e: IndexOutOfBoundsException) {
                return super.onMeasure(recycler, state, widthSpec, heightSpec)
            }
        }

        measuredWidth = when (widthMode) {
            View.MeasureSpec.EXACTLY -> {
                if (calculatedWidth.isNotZero() && orientation == HORIZONTAL) {
                    if (widthSize < desiredSize) {
                        min(calculatedWidth, widthSize)
                    } else desiredSize
                } else {
                    widthSize
                }
            }
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> {
                min(calculatedWidth, width)
            }
            else -> 0
        }

        measuredHeight = when (heightMode) {
            View.MeasureSpec.EXACTLY -> {
                if (calculatedHeight.isNotZero() && orientation == VERTICAL) {
                    if (heightSize < desiredSize) {
                        min(calculatedHeight, heightSize)
                    } else desiredSize
                } else heightSize
            }
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> {
                min(calculatedHeight, height)
            }
            else -> 0
        }

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return false
    }

    @Throws(IndexOutOfBoundsException::class)
    private fun measureScrapChild(
        recycler: RecyclerView.Recycler,
        position: Int,
        widthSpec: Int,
        heightSpec: Int,
        measuredDimension: IntArray
    ) {
        val view: View = recycler.getViewForPosition(position)
        val p = view.layoutParams
        val childWidthSpec: Int = ViewGroup.getChildMeasureSpec(
            widthSpec,
            paddingLeft + paddingRight, p.width
        )
        val childHeightSpec: Int = ViewGroup.getChildMeasureSpec(
            heightSpec,
            paddingTop + paddingBottom, p.height
        )
        view.measure(childWidthSpec, childHeightSpec)
        measuredDimension[0] = view.measuredWidth
        measuredDimension[1] = view.measuredHeight
        recycler.recycleView(view)
    }

    override fun supportsPredictiveItemAnimations() = false
}