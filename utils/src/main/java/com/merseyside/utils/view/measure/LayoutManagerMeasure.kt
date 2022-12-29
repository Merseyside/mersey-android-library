package com.merseyside.utils.view.measure

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

context(RecyclerView.LayoutManager)
fun RecyclerView.Recycler.measureScrapChild(
    position: Int,
    widthSpec: Int,
    heightSpec: Int,
    measuredDimension: IntArray
) {
    val view: View = getViewForPosition(position)
    val p = view.layoutParams
    val childWidthSpec: Int = ViewGroup.getChildMeasureSpec(
        widthSpec, paddingLeft + paddingRight, p.width
    )
    val childHeightSpec: Int = ViewGroup.getChildMeasureSpec(
        heightSpec, paddingTop + paddingBottom, p.height
    )
    view.measure(childWidthSpec, childHeightSpec)
    measuredDimension[0] = view.measuredWidth
    measuredDimension[1] = view.measuredHeight
    recycleView(view)
}

context(RecyclerView.LayoutManager)
fun RecyclerView.Recycler.calculateAllChildrenSize(
    widthSpec: Int,
    heightSpec: Int,
    totalSizes: IntArray // must be IntArray with length of 2
) {
    val childDesiredSizes = IntArray(2)
    (0 until itemCount).forEach { index ->
        measureScrapChild(
            index,
            widthSpec,
            heightSpec,
            childDesiredSizes
        )

        totalSizes[0] = totalSizes[0] + childDesiredSizes[0]
        totalSizes[1] = totalSizes[1] + childDesiredSizes[1]
    }
}

context(RecyclerView.LayoutManager)
fun RecyclerView.Recycler.calculateMaxChildrenSize(
    widthSpec: Int,
    heightSpec: Int,
    maxSizes: IntArray // must be IntArray with length of 2
) {
    val childDesiredSizes = IntArray(2)
    (0 until itemCount).forEach { index ->
        measureScrapChild(
            index,
            widthSpec,
            heightSpec,
            childDesiredSizes
        )

        maxSizes[0] = max(maxSizes[0], childDesiredSizes[0])
        maxSizes[1] = max(maxSizes[1], childDesiredSizes[1])
    }
}

context(RecyclerView.LayoutManager)
fun RecyclerView.Recycler.calculateTotalDesiredChildrenSizes(totalSize: IntArray) {
    calculateAllChildrenSize(
        View.MeasureSpec.makeMeasureSpec(ANY_SIZE, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(ANY_SIZE, View.MeasureSpec.UNSPECIFIED),
        totalSize
    )
}

context(RecyclerView.LayoutManager)
fun RecyclerView.Recycler.calculateMaxDesiredChildrenSizes(totalSize: IntArray) {
    calculateMaxChildrenSize(
        View.MeasureSpec.makeMeasureSpec(ANY_SIZE, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(ANY_SIZE, View.MeasureSpec.UNSPECIFIED),
        totalSize
    )
}

private const val ANY_SIZE = 0 // doesn't matter what ize we pass

