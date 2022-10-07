package com.merseyside.adapters.feature.composable

import com.merseyside.adapters.feature.composable.adapter.ViewCompositeAdapter
import com.merseyside.adapters.feature.composable.delegate.ViewDelegateAdapter
import com.merseyside.adapters.feature.composable.dsl.context.ComposeContext
import com.merseyside.adapters.feature.composable.model.ViewAdapterViewModel
import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.utils.runWithDefault

interface HasComposableAdapter<Model>
        where Model : ViewAdapterViewModel {

    val adapter: ViewCompositeAdapter<SCV, Model>
    val delegates: List<ViewDelegateAdapter<out SCV, *, out Model>>

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

interface HasSimpleComposableAdapter : HasComposableAdapter<ViewAdapterViewModel> {

    override val adapter: ViewCompositeAdapter<SCV, ViewAdapterViewModel>
}