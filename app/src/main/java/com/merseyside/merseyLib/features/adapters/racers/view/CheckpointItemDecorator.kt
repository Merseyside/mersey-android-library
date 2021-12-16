package com.merseyside.merseyLib.features.adapters.racers.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.utils.convertDpToPixel
import com.merseyside.utils.ext.getDimension

class CheckpointItemDecorator(context: Context, spacing: Float) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes res: Int): this(context, context.getDimension(res))

    private val mPadding: Int = convertDpToPixel(context, spacing).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        if (parent.adapter == null) return

        if (itemPosition != 0) {
            outRect.top = mPadding
        }
    }
}