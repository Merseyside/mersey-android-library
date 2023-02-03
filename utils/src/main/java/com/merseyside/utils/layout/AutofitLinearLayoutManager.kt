package com.merseyside.utils.layout

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

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

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        if (orientation == HORIZONTAL) lp.width = width / itemCount
        else lp.height = height / itemCount

        return true
    }
}