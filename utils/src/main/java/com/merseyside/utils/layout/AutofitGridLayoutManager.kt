package com.merseyside.utils.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.utils.math.ceilInt
import com.merseyside.utils.view.measure.measureDesiredScrapChild

/**
 * Uses when we need to make all items visible inside recycler view without scrolling by
 * set them all equal sizes. Should be used only if we sure all items are the same size.
 *
 * The second goal
 */
class AutofitGridLayoutManager : GridLayoutManager {

    var childMeasuredWidth = 0
    var childMeasuredHeight = 0

    constructor(
        context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean = false
    ): super(context, spanCount, orientation, reverseLayout)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(
        context: Context,
        spanCount: Int
    ) : super(context, spanCount)

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        val widthMode = MeasureSpec.getMode(widthSpec)
        val heightMode = MeasureSpec.getMode(heightSpec)
        val widthSize = MeasureSpec.getSize(widthSpec)
        val heightSize = MeasureSpec.getSize(heightSpec)

//        logMeasureSpec(widthSpec, "width")
//        logMeasureSpec(heightSpec, "height")

        val itemsCount = state.itemCount
        if (itemsCount != 0) {

            val itemsInRow = itemsCount / spanCount

            val desiredSizes = IntArray(2)
            recycler.measureDesiredScrapChild(0, desiredSizes) // ask child for desired sizes

            if (orientation == VERTICAL) {

                if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
                    val maxItemSize = ceilInt(heightSize / itemsInRow.toFloat())
                    if (desiredSizes[1] > maxItemSize) childMeasuredHeight = maxItemSize
                }

            } else {
                if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
                    val maxItemSize = ceilInt(widthSize / itemsInRow.toFloat())
                    if (desiredSizes[0] > maxItemSize) childMeasuredWidth = maxItemSize
                }

            }
        }

        super.onMeasure(recycler, state, widthSpec, heightSpec)
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        if (childMeasuredWidth != 0) lp.width = childMeasuredWidth
        if (childMeasuredHeight != 0) lp.height = childMeasuredHeight

        return true
    }
}