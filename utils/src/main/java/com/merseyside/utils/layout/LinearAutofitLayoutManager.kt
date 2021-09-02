package com.merseyside.utils.layout

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class LinearAutofitLayoutManager : WrapContentLinearLayoutManager, SizeProviderLayoutManager {

    override var itemWidth: Int = 0
    override var itemHeight: Int = 0

    private var lastWidth = 0
    private var lastHeight = 0

    private var maxSize: Int = Int.MAX_VALUE

    private var lastItemCount = 0
        set(value) {
            if (value != field) {
                field = value
                requestLayout()
            }
        }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(
        @NonNull context: Context,
        orientation: Int = HORIZONTAL,
        reverseLayout: Boolean = false,
        maxSize: Int = Int.MAX_VALUE
    ) : super(context, orientation, reverseLayout) {
        this.maxSize = maxSize
    }

    fun setMaxSize(maxSize: Int) {
        this.maxSize = maxSize
    }

    fun resetMaxSize() {
        this.maxSize = Int.MAX_VALUE
    }

    private fun calculateItemSizes() {
        if (lastItemCount != 0) {
            if (orientation == HORIZONTAL) {
                itemWidth = min((lastWidth - paddingStart - paddingEnd) / lastItemCount, maxSize)
                itemHeight = lastHeight - paddingTop - paddingBottom
            } else {
                itemWidth = lastWidth - paddingStart - paddingEnd
                itemHeight = min((lastHeight - paddingTop - paddingBottom) / lastItemCount, maxSize)
            }
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (lastWidth != width || lastHeight != height || lastItemCount != itemCount) {

            lastWidth = width
            lastHeight = height
            lastItemCount = itemCount

            calculateItemSizes()

            requestLayout()
        }

        super.onLayoutChildren(recycler, state)
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        lp.width = itemWidth
        lp.height = itemHeight
        return true
    }
}