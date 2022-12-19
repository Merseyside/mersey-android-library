package com.merseyside.adapters.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.R

open class SimpleItemOffsetDecorator(
    private val top: Int = 0,
    private val left: Int = 0,
    private val right: Int = 0,
    private val bottom: Int = 0
) : RecyclerView.ItemDecoration() {

    constructor(
        verticalOffset: Int = 0,
        horizontalOffset: Int = 0
    ): this(verticalOffset, horizontalOffset, horizontalOffset, verticalOffset)

    constructor(
        context: Context,
        @DimenRes topRes: Int = R.dimen.decorator_zero_size,
        @DimenRes leftRes: Int = R.dimen.decorator_zero_size,
        @DimenRes rightRes: Int = R.dimen.decorator_zero_size,
        @DimenRes bottomRes: Int = R.dimen.decorator_zero_size
    ) : this(
        context.resources.getDimension(topRes).toInt(),
        context.resources.getDimension(leftRes).toInt(),
        context.resources.getDimension(rightRes).toInt(),
        context.resources.getDimension(bottomRes).toInt()
    )

    constructor(
        context: Context,
        resourcesOffsets: ResourcesOffsets
    ): this(
        context = context,
        topRes = resourcesOffsets.top,
        leftRes = resourcesOffsets.left,
        rightRes = resourcesOffsets.right,
        bottomRes = resourcesOffsets.bottom
    )

    constructor(
        context: Context,
        @DimenRes verticalOffsetRes: Int = R.dimen.decorator_zero_size,
        @DimenRes horizontalOffsetRes: Int = R.dimen.decorator_zero_size
    ) : this(context, verticalOffsetRes, horizontalOffsetRes, horizontalOffsetRes, verticalOffsetRes)

    constructor(
        context: Context,
        @DimenRes offsetRes: Int = R.dimen.decorator_zero_size
    ) : this(context, offsetRes, offsetRes)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = top
        outRect.left = left
        outRect.right = right
        outRect.bottom = bottom
    }
}