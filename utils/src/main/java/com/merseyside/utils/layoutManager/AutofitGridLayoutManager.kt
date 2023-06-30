package com.merseyside.utils.layoutManager

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.utils.math.ceilInt

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

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {

        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, heightMode)
        //logMeasureSpec(heightMeasureSpec, "height")

        val itemsCount = state.itemCount
        if (itemsCount != 0) {

            val itemsInRow = itemsCount / spanCount

            val desiredSizes = IntArray(2)
            //recycler.measureDesiredScrapChild(0, desiredSizes) // ask child for desired sizes

            if (orientation == VERTICAL) {

                if (heightMode == View.MeasureSpec.EXACTLY || heightMode == View.MeasureSpec.AT_MOST) {
                    val maxItemSize = ceilInt(height / itemsInRow.toFloat())

                    childMeasuredHeight = maxItemSize
                }

            } else {
                if (widthMode == View.MeasureSpec.EXACTLY || widthMode == View.MeasureSpec.AT_MOST) {
                    val maxItemSize = ceilInt(width / itemsInRow.toFloat())
                    if (desiredSizes[0] > maxItemSize) childMeasuredWidth = maxItemSize
                }

            }
        }

        super.onLayoutChildren(recycler, state)
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        if (childMeasuredWidth != 0) lp.width = childMeasuredWidth
        if (childMeasuredHeight != 0) lp.height = childMeasuredHeight

        return true
    }
}