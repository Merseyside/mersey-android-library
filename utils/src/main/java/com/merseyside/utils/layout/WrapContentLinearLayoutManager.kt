package com.merseyside.utils.layout

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WrapContentLinearLayoutManager(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
): LinearLayoutManager(context, attrs, defStyleAttr, defStyleRes) {

    @Throws(IndexOutOfBoundsException::class)
    constructor(context: Context) : this(
        context,
        null, 0, 0
    )
        override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (ignored: IndexOutOfBoundsException) {}
        }

        override fun supportsPredictiveItemAnimations() = false
}