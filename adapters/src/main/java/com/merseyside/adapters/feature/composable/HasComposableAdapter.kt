package com.merseyside.adapters.feature.composable

import com.merseyside.adapters.feature.composable.adapter.ViewCompositeAdapter
import com.merseyside.adapters.feature.composable.delegate.ViewAdapterViewModel
import com.merseyside.adapters.feature.composable.delegate.ViewDelegateAdapter
import com.merseyside.adapters.feature.composable.dsl.context.ComposeContext
import com.merseyside.adapters.utils.runWithDefault

interface HasComposableAdapter {

    val adapter: ViewCompositeAdapter
    val delegates: List<ViewDelegateAdapter<out SCV, *, out ViewAdapterViewModel>>

    suspend fun composeScreen(): ComposeContext

    suspend fun composeInternal() = runWithDefault {
        if (adapter.delegatesManager.isEmpty()) {
            adapter.delegatesManager.addDelegateList(delegates)
        }

        val screenContext = composeScreen()
        showViews(screenContext.views)
    }

    fun showViews(views: List<SCV>) {
        adapter.addOrUpdateAsync(views)
    }

    fun invalidateAsync(onComplete: (Unit) -> Unit = {}) {
        adapter.doAsync(onComplete) { invalidate() }
    }

    suspend fun invalidate() {
        composeInternal()
    }
}