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
        context: Context,
        @DimenRes top: Int = R.dimen.decorator_zero_size,
        @DimenRes left: Int = R.dimen.decorator_zero_size,
        @DimenRes right: Int = R.dimen.decorator_zero_size,
        @DimenRes bottom: Int = R.dimen.decorator_zero_size
    ) : this(
        context.resources.getDimension(top).toInt(),
        context.resources.getDimension(left).toInt(),
        context.resources.getDimension(right).toInt(),
        context.resources.getDimension(bottom).toInt()
    )

    constructor(
        context: Context,
        resourcesOffsets: ResourcesOffsets
    ): this(
        context = context,
        top = resourcesOffsets.top,
        left = resourcesOffsets.left,
        right = resourcesOffsets.right,
        bottom = resourcesOffsets.bottom
    )

    constructor(
        context: Context,
        @DimenRes verticalOffset: Int = R.dimen.decorator_zero_size,
        @DimenRes horizontalOffset: Int = R.dimen.decorator_zero_size
    ) : this(context, verticalOffset, horizontalOffset, horizontalOffset, verticalOffset)

    constructor(
        context: Context,
        @DimenRes offset: Int = R.dimen.decorator_zero_size
    ) : this(context, offset, offset)

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