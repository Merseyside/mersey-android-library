package com.merseyside.adapters.feature.composable

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.merseyside.adapters.compose.adapter.ViewCompositeAdapter
import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.dsl.context.compose
import com.merseyside.adapters.compose.model.ViewAdapterViewModel
import com.merseyside.adapters.compose.style.ComposingStyle
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.viewProvider.ViewComposeContext
import com.merseyside.adapters.interfaces.ext.addOrUpdateAsync

interface HasComposableAdapter {

    val adapter: ViewCompositeAdapter<SCV, ViewAdapterViewModel>
    val delegates: List<ViewDelegateAdapter<out SCV, out ComposingStyle, out ViewAdapterViewModel>>

    val context: Context
    val viewLifecycleOwner: LifecycleOwner

    suspend fun composeScreen(): ComposeContext.() -> Unit

    suspend fun composeInternal() {
        if (adapter.delegatesManager.isEmpty()) {
            adapter.delegatesManager.addDelegateList(delegates)
        }

        val screenContext = compose(context, viewLifecycleOwner, composeScreen()).apply {
            relativeAdapter = adapter
        }

        showViews(screenContext.views)
    }

    fun showViews(views: List<SCV>) {
        adapter.addOrUpdateAsync(views)
    }

    fun invalidateAsync(onComplete: (Unit) -> Unit = {}) {
        adapter.workManager.doAsync(onComplete) { invalidate() }
    }

    @Suppress("UNCHECKED_CAST")
    fun <View : SCV> findViewById(id: String): View? {
        return adapter.findViewById(id)
    }

    suspend fun invalidate() {
        composeInternal()
    }
}