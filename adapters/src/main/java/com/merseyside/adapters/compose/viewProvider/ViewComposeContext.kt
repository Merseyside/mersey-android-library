package com.merseyside.adapters.compose.viewProvider

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.merseyLib.kotlin.logger.ILogger

abstract class ViewComposeContext<View: SCV>(
    val context: Context,
    val viewLifecycleOwner: LifecycleOwner,
) : ILogger {

    private lateinit var mutViews: MutableList<View>
    val views: List<View>
        get() {
            if (!::mutViews.isInitialized) {
                mutViews = ArrayList()
                build()
            }

            return mutViews
        }

    protected abstract fun build()

    protected abstract fun onUpdated(views: List<View>)

    protected fun update() {
        internalUpdate()
        onUpdated(views)
    }

    internal open fun add(view: View) {
        mutViews.add(view)
    }

    internal open fun add(views: List<View>) {
        mutViews.addAll(views)
    }

    private fun internalUpdate() {
        mutViews.clear()
        build()
    }
}

typealias ViewProviderContext = ViewComposeContext<SCV>

context(ViewProviderContext)
fun <View : SCV> View.addView(): View {
    add(this)
    return this
}