package com.merseyside.utils.pagination

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.kotlin.utils.ifFalse
import com.merseyside.merseyLib.kotlin.utils.ifTrue
import com.merseyside.merseyLib.kotlin.utils.safeLet
import com.merseyside.utils.layout.findLastVisibleItemPosition

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
                requireRecycler {
                    val position = getChildAdapterPosition(view)
                    loadNextPageIfNeed(position)
                    loadPrevPageIfNeed(position)
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) {}
        }

    private val hasItems: Boolean
        get() = requireRecycler { childCount != 0 }

    abstract val onLoadFirstPage: (onComplete: () -> Unit) -> Unit
    abstract val onLoadNextPage: (onComplete: () -> Unit) -> Unit
    abstract val onLoadPrevPage: (onComplete: () -> Unit) -> Unit

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
            loadFirstPage()
        }
        isPaging = true
    }

    fun <R> ifRecyclerNotNull(block: RecyclerView.() -> R): R? {
        return try {
            requireRecycler(block)
        } catch (_: NullPointerException) {
            Logger.logErr("Can not start paging because recycler didn't set!")
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

    private fun loadFirstPage() = startLoading { complete ->
        onLoadFirstPage {
            layoutManager?.let { manager ->
                loadNextPageIfNeed(manager.findLastVisibleItemPosition()).ifFalse(complete)
            }
        }
    }

    private fun loadNextPage() = startLoading { complete ->
        onLoadNextPage {
            layoutManager?.let { manager ->
                loadNextPageIfNeed(manager.findLastVisibleItemPosition()).ifFalse(complete)
            }
        }
    }

    private fun loadPrevPage() = startLoading { complete ->
        onLoadPrevPage {
            layoutManager?.let { manager ->
                loadPrevPageIfNeed(manager.findLastVisibleItemPosition()).ifFalse(complete)
            }
        }
    }

    private fun startLoading(block: RecyclerView.(completeLoading: () -> Unit) -> Unit) = requireRecycler {
        removeOnChildAttachStateChangeListener(childStateListener)
        val completeLoading = { addOnChildAttachStateChangeListener(childStateListener) }
        block(completeLoading)
    }

    private fun needToLoadNextPage(lastPosition: Int): Boolean = requireRecycler {
        val itemCount = adapter?.itemCount
        safeLet(itemCount) { counts ->
            (counts - lastPosition) <= loadItemsCountDownOffset
        } ?: false
    }

    private fun needToLoadPrevPage(firstPosition: Int): Boolean {
        return firstPosition == loadItemsCountUpOffset
    }

    private fun loadNextPageIfNeed(position: Int): Boolean {
        return needToLoadNextPage(position).ifTrue {
            loadNextPage()
        }
    }

    private fun loadPrevPageIfNeed(position: Int): Boolean {
        return needToLoadPrevPage(position).ifTrue {
            loadPrevPage()
        }
    }
}