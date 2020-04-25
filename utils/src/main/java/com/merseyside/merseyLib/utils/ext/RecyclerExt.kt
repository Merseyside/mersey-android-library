package com.merseyside.merseyLib.utils.ext

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.merseyside.merseyLib.utils.SnapOnScrollListener

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

fun RecyclerView.attachSnapHelperWithListener(
    snapHelper: SnapHelper,
    behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
    onSnapPositionChangeListener: SnapOnScrollListener.OnSnapPositionChangeListener?
) {
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener = SnapOnScrollListener(snapHelper, behavior, onSnapPositionChangeListener)
    addOnScrollListener(snapOnScrollListener)
}

fun RecyclerView.smoothScrollToStart() {
    if (layoutManager != null) {
        smoothScrollToPosition(0)
    }
}

fun RecyclerView.scrollToStart() {
    if (layoutManager != null) {
        scrollToPosition(0)
    }
}

fun RecyclerView.scrollToEnd() {
    if (layoutManager != null) {
        scrollToPosition(childCount - 1)
    }
}

fun RecyclerView.smoothScrollToEnd() {
    if (layoutManager != null) {
        smoothScrollToPosition(childCount - 1)
    }
}