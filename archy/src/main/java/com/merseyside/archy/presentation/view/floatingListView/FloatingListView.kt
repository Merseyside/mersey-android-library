package com.merseyside.archy.presentation.view.floatingListView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.archy.R
import com.merseyside.archy.databinding.ViewFloatingListBinding
import com.merseyside.archy.presentation.view.HORIZONTAL
import com.merseyside.archy.presentation.view.VERTICAL

import com.merseyside.utils.delegate.viewBinding
import com.merseyside.utils.layoutManager.GridLayoutManager
import com.merseyside.utils.layoutManager.LinearLayoutManager

/**
 * This class have to be extended from RelativeLayout
 */
class FloatingListView(context: Context, attrsSet: AttributeSet? = null) :
    RelativeLayout(context, attrsSet) {

    private val binding: ViewFloatingListBinding by viewBinding(R.layout.view_floating_list)

    enum class Orientation { VERTICAL, HORIZONTAL, GRID }

    private lateinit var recyclerView: RecyclerView
    private var relativeView: View? = null
    var orientation: Orientation = Orientation.VERTICAL

    @IdRes
    var containerId: Int = 0

    private var adapter: RecyclerView.Adapter<*>? = null

    init {
        if (attrsSet != null) {
            loadAttributes(attrsSet)
        }

        doLayout()
    }

    private fun loadAttributes(attrsSet: AttributeSet) {
        //val array = context.theme.obtainStyledAttributes(attrsSet, R.styleable.FloatingListView, 0, 0)
    }

    private fun applyOrientation() {
        recyclerView.apply {
            layoutManager = when (orientation) {
                Orientation.VERTICAL -> LinearLayoutManager(
                    context,
                    VERTICAL,
                    false
                )

                Orientation.HORIZONTAL -> LinearLayoutManager(
                    context,
                    HORIZONTAL,
                    false
                )
                Orientation.GRID -> GridLayoutManager(context, 2)
            }
        }
    }

    private fun doLayout() {
        //inflate(context, R.layout.view_floating_list, this)

        recyclerView = binding.floatingList
        applyOrientation()

        if (relativeView != null) {
            build()
        }

    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
        this.adapter = adapter
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun build() {
        visibility = GONE

        relativeView?.viewTreeObserver?.addOnGlobalLayoutListener(layoutListener)
    }

    private fun removeListener() {
        relativeView!!.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
    }

    private val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        removeView()

        // position
        val rootView = if (containerId == 0) {
            relativeView!!.rootView as ViewGroup
        } else {
            relativeView!!.rootView.findViewById(containerId) as ViewGroup
        }

        val layoutParams = LayoutParams(
            rootView.measuredWidth,
            rootView.measuredHeight
        )

        layoutParams.addRule(ALIGN_PARENT_TOP)
        layoutParams.addRule(ALIGN_PARENT_LEFT)

        rootView.addView(this, layoutParams)

        removeListener()
    }

    fun setRelativeView(relativeView: View) {
        this.relativeView = relativeView

        build()
    }

    fun prepare() {

        if (relativeView != null) {
            if (visibility == View.VISIBLE) return

            val relativeRootView = if (containerId == 0) {
                rootView
            } else {
                rootView.findViewById(containerId)
            }


            val rootCoord: IntArray = calculateViewCoords(rootView, relativeRootView)
            val globalRootCoords: IntArray = calculateViewCoords(rootView, relativeView!!)

            val rootViewBottomPosition = relativeRootView.measuredHeight + rootCoord[1]
            val relativeViewBottomPosition = globalRootCoords[1] + relativeView!!.measuredHeight

            val rootViewCenter = (rootCoord[1] + relativeRootView.measuredHeight / 2)

            val layoutParams =
                recyclerView.layoutParams as MarginLayoutParams
            if (globalRootCoords[1] < rootViewCenter) { // to bottom
                val rect = Rect()
                val coord: IntArray = calculateViewCoords(relativeRootView, relativeView!!, rect)

                layoutParams.topMargin = coord[1] - rootCoord[1] + relativeView!!.height

                if (containerId == 0) {
                    layoutParams.bottomMargin = rootView.height - rect.bottom
                }
            } else { // to top

                if (containerId == 0) {
                    layoutParams.bottomMargin =
                        rootView.height - globalRootCoords[1] - relativeView!!.height
                } else {
                    layoutParams.bottomMargin =
                        relativeView!!.height + rootViewBottomPosition - relativeViewBottomPosition
                }
            }

        } else {
            throw IllegalArgumentException("Relative view is null")
        }

    }

    fun removeView() {
        if (parent != null) {
            (parent as ViewGroup).removeView(this)
        }
    }

    private fun calculateViewCoords(
        rootView: View,
        view: View
    ): IntArray {
        return calculateViewCoords(rootView, view, Rect())
    }

    private fun calculateViewCoords(
        rootView: View,
        view: View,
        rect: Rect
    ): IntArray {
        rootView.getWindowVisibleDisplayFrame(rect)
        val coords = IntArray(2)
        view.getLocationInWindow(coords)
        return coords
    }
}