package com.merseyside.adapters.compose.dsl.context

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.merseyside.adapters.compose.view.base.SCV

object listContext {
    context(ComposeContext)
    operator fun invoke(
        contextId: String,
        buildViews: ComposeContext.() -> Unit
    ): ListComposeContext {
        return getOrCreateChildContext(contextId) { id, context, viewLifecycleOwner ->
            ListComposeContext(id, context, viewLifecycleOwner, buildViews)
        }
    }
}

open class ListComposeContext(
    contextId: String,
    context: Context,
    viewLifecycleOwner: LifecycleOwner,
    buildViews: ComposeContext.() -> Unit
): ViewGroupComposeContext<SCV>(contextId, context, viewLifecycleOwner, buildViews)