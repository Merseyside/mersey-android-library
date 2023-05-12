package com.merseyside.utils.pagination

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.kotlin.utils.safeLet

abstract class PaginationScrollHandler(
    protected val loadItemsCountDownOffset: Int,
    protected val loadItemsCountUpOffset: Int
) {

    var isPaging: Boolean = false
        private set

    private var recyclerView: RecyclerView? = null

    private val childStateListener: RecyclerView.OnChildAttachStateChangeListener =
        object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                if (needToLoadDownNextPage(view)) onLoadNextPage()
                if (needToLoadUpNextPage(view)) onLoadPrevPage()
            }

            override fun onChildViewDetachedFromWindow(view: View) {}

            private fun needToLoadDownNextPage(view: View): Boolean {
                return requireRecycler {
                    val lastPosition = getChildAdapterPosition(view)
                    val itemCount = adapter?.itemCount
                    safeLet(itemCount) { counts ->
                        (counts - lastPosition) <= loadItemsCountDownOffset
                    } ?: false
                }
            }

            private fun needToLoadUpNextPage(view: View): Boolean {
                return requireRecycler {
                    val lastPosition = getChildAdapterPosition(view)
                    lastPosition == loadItemsCountUpOffset
                }
            }
        }

    private val hasItems: Boolean
        get() = requireRecycler { childCount != 0 }

    abstract val onLoadFirstPage: (onComplete: () -> Unit) -> Unit
    abstract val onLoadNextPage: () -> Unit
    abstract val onLoadPrevPage: () -> Unit

    fun setRecyclerView(recyclerView: RecyclerView?) {
        val prev = this.recyclerView
        this.recyclerView = recyclerView
        if (prev == null && isPaging) {
            startPaging()
        } else if (prev != null) {
            reset()
        }
    }

    fun startPaging() {
        ifRecyclerNotNull {
            onLoadFirstPage {
                addOnChildAttachStateChangeListener(childStateListener)
            }
        }
        isPaging = true
    }

    fun <R> ifRecyclerNotNull(block: RecyclerView.() -> R): R? {
        return try {
            requireRecycler(block)
        } catch (_: NullPointerException) {
            null
        }
    }

    fun <R> requireRecycler(block: RecyclerView.() -> R): R {
        return recyclerView?.let {
            block(it)
        } ?: throw NullPointerException("Recycler view hasn't set!")
    }

    /**
     * Sets pagination to initial state
     * Also removes child state listener.
     * @see startPaging have to be called again.
     */
    fun reset() {
        isPaging = false
        recyclerView?.removeOnChildAttachStateChangeListener(childStateListener)
    }
}