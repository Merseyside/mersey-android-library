package com.merseyside.utils.layout

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

open class LinearLayoutManager : LinearLayoutManager {

    private var gravity: Gravity? = null

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(
        context: Context,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(context, orientation, reverseLayout)

    constructor(context: Context) : this(
        context,
        null, 0, 0
    )

    constructor(context: Context, gravity: Gravity) : this(
        context
    ) {
        this.gravity = gravity
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
            if (gravity != null) {
                (0 until itemCount).forEach { index ->

                }
            }
        } catch (ignored: IndexOutOfBoundsException) {}
    }

    private fun setRecyclerItemGravity(
        recycler: Recycler,
        position: Int
    ) {
        val view = recycler.getViewForPosition(position)
        val layoutParams = view.layoutParams as LinearLayout.LayoutParams
//        layoutParams.apply {
//            layout
//        }
    }

    override fun supportsPredictiveItemAnimations() = false
}