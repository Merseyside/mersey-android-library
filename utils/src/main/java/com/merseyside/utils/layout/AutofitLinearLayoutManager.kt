package com.merseyside.utils.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.utils.view.measure.calculateMaxDesiredChildrenSizes
import com.merseyside.utils.view.measure.calculateTotalDesiredChildrenSizes
import kotlin.math.min

/**
 * Uses when we need to make all items visible inside recycler view without scrolling by
 * set them all equal sizes. Should be used only if we sure all items are the same size.
 */
class AutofitLinearLayoutManager : LinearLayoutManager {

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(
        context: Context,
        orientation: Int = HORIZONTAL,
        reverseLayout: Boolean = false,
    ) : super(context, orientation, reverseLayout)

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

        val measuredRecyclerWidth: Int
        val measuredRecyclerHeight: Int

        val totalDesiredSize = IntArray(2)
        recycler.calculateTotalDesiredChildrenSizes(totalDesiredSize) // calculate children desired total sizes

        val maxDesiredSize = IntArray(2)
        recycler.calculateMaxDesiredChildrenSizes(maxDesiredSize) // calculate children desired max sizes

        if (orientation == HORIZONTAL) {
            measuredRecyclerWidth = when (widthMode) {
                MeasureSpec.EXACTLY -> widthSize
                MeasureSpec.AT_MOST -> min(widthSize, totalDesiredSize.first()) // desired items size or AT_MOST size
                MeasureSpec.UNSPECIFIED -> totalDesiredSize.first() // recycler size is unspecified, so ask children items first how big they may to be.
                else -> throw IllegalArgumentException("Should never happened.")
            }

            measuredRecyclerHeight = when(heightMode) {
                MeasureSpec.EXACTLY -> heightSize
                MeasureSpec.AT_MOST -> min(heightSize, maxDesiredSize.last()) // max items size or AT_MOST size
                MeasureSpec.UNSPECIFIED -> maxDesiredSize.last()
                else -> throw IllegalArgumentException("Should never happened.")
            }
        } else {
            measuredRecyclerWidth = when (widthMode) {
                MeasureSpec.EXACTLY -> widthSize
                MeasureSpec.AT_MOST -> min(widthSize, maxDesiredSize.first()) // desired items size or AT_MOST size
                MeasureSpec.UNSPECIFIED -> maxDesiredSize.first() // recycler size is unspecified, so ask children items first how big they may to be.
                else -> throw IllegalArgumentException("Should never happened.")
            }

            measuredRecyclerHeight = when(heightMode) {
                MeasureSpec.EXACTLY -> heightSize
                MeasureSpec.AT_MOST -> min(heightSize, totalDesiredSize.last()) // desired items size or AT_MOST size
                MeasureSpec.UNSPECIFIED -> totalDesiredSize.last()
                else -> throw IllegalArgumentException("Should never happened.")
            }
        }

        setMeasuredDimension(measuredRecyclerWidth, measuredRecyclerHeight)
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        if (orientation == HORIZONTAL) lp.width = width / itemCount
        else lp.height = height / itemCount

        return true
    }

    override fun isAutoMeasureEnabled() = false
}