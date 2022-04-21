package com.merseyside.utils.layout

import android.content.Context
import android.util.TypedValue
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min


class GridAutofitLayoutManager : GridLayoutManager {
    private var columnWidth = 0
    private var isColumnWidthChanged = true
    private var lastWidth = 0
    private var lastHeight = 0
    private var maxColumnRowCount = 0

    constructor(context: Context, columnWidth: Int) : super(context, 1) {
        /* Initially set spanCount to 1, will be changed automatically later. */
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    constructor(
        context: Context,
        columnWidth: Int,
        orientation: Int,
        reverseLayout: Boolean,
        maxColumnRowCount: Int = 0
    ) : super(context, 1, orientation, reverseLayout) {

        this.maxColumnRowCount = maxColumnRowCount
        /* Initially set spanCount to 1, will be changed automatically later. */
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    private fun checkedColumnWidth(context: Context, columnWidth: Int): Int {
        var mutColumnWidth = columnWidth
        if (mutColumnWidth <= 0) {
            /* Set default columnWidth value (48dp here). It is better to move this constant
            to static constant on top, but we need context to convert it to dp, so can't really
            do so. */
            mutColumnWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 48F,
                context.resources.displayMetrics
            ).toInt()
        }
        return mutColumnWidth
    }

    fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth
            isColumnWidthChanged = true
        }
    }

    override fun onLayoutChildren(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) {
        val width = width
        val height = height
        if (columnWidth > 0 && width > 0 && height > 0 && (isColumnWidthChanged || lastWidth != width || lastHeight != height)) {
            val totalSpace: Int = if (orientation == LinearLayoutManager.VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }

            val spanCount = if (maxColumnRowCount == 0) {
                max(1, totalSpace / columnWidth)
            } else {
                min(maxColumnRowCount, totalSpace / columnWidth)
            }

            setSpanCount(spanCount)
            isColumnWidthChanged = false
        }
        lastWidth = width
        lastHeight = height
        super.onLayoutChildren(recycler, state)
    }
}