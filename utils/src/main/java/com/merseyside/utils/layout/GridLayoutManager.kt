package com.merseyside.utils.layout

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridLayoutManager constructor(
    context: Context,
    count: Int,
    orientation: Int,
    reverseLayout: Boolean
) : GridLayoutManager(context, count, orientation, reverseLayout) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (ignored: IndexOutOfBoundsException) {}
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}