package com.merseyside.adapters.feature.compositeScreen

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.merseyside.adapters.feature.compositeScreen.adapter.ViewCompositeAdapter
import com.merseyside.adapters.feature.compositeScreen.delegate.ViewAdapterViewModel
import com.merseyside.adapters.feature.compositeScreen.delegate.ViewDelegate
import com.merseyside.adapters.feature.compositeScreen.delegate.ViewDelegateAdapter
import com.merseyside.adapters.feature.compositeScreen.dsl.context.ComposeContext
import com.merseyside.adapters.utils.runWithDefault
import com.merseyside.merseyLib.kotlin.logger.ILogger

abstract class ScreenComposer(
    val adapter: ViewCompositeAdapter,
    val viewLifecleOwner: LifecycleOwner
) : ILogger {

    abstract val delegates: List<ViewDelegateAdapter<out SCV, *, out ViewAdapterViewModel>>

    abstract suspend fun composeScreen(): ComposeContext

    private suspend fun composeInternal() = runWithDefault {
        if (adapter.delegatesManager.isEmpty()) {
            adapter.delegatesManager.addDelegateList(delegates)
        }

        val screenContext = composeScreen()
        showViews(screenContext.views)
    }

    open fun showViews(views: List<SCV>) {
        adapter.addOrUpdateAsync(views)
    }

    protected fun addDataSource(ld: LiveData<*>) {
        ld.observe(viewLifecleOwner) { buildAsync() }
    }

    fun buildAsync(onComplete: (Unit) -> Unit = {}) {
        adapter.doAsync(onComplete) { build() }
    }

    suspend fun build() {
        composeInternal()
    }

    override val tag: String = "CompositeScreenBuilder"
}


