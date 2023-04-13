package com.merseyside.utils.layout

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class LinearLayoutManager : LinearLayoutManager {

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(
        context: Context,
        orientation: Int,
        reverseLayout: Boolean = false
    ) : super(context, orientation, reverseLayout)

    constructor(context: Context) : this(context, null, 0, 0)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (ignored: IndexOutOfBoundsException) {}
    }

    override fun supportsPredictiveItemAnimations() = false
}