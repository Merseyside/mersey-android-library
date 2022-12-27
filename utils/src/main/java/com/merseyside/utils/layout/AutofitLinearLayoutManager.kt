package com.merseyside.utils.layout

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class AutofitLinearLayoutManager : WrapContentLinearLayoutManager, SizeProviderLayoutManager {

    override var itemWidth: Int = 0
    override var itemHeight: Int = 0

    private var lastWidth = 0
    private var lastHeight = 0

    private var maxSize: Int = Int.MAX_VALUE

    private var lastItemCount = 0

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
        desiredItemSize: Int = 0,
        maxSize: Int = Int.MAX_VALUE
    ) : super(context, orientation, reverseLayout, desiredItemSize) {
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

    private fun setupNewValues(): Boolean {
        return if (lastWidth != measuredWidth || lastHeight != measuredHeight || lastItemCount != itemCount) {
            lastWidth = measuredWidth
            lastHeight = measuredHeight
            lastItemCount = itemCount

            calculateItemSizes()
            true
        } else false
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        setupNewValues()

        lp.width = itemWidth
        lp.height = itemHeight

        return true
    }
}