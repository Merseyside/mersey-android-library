package com.merseyside.adapters.compose.dsl.context

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.viewProvider.ViewComposeContext

object selectableListContext {
    context(ComposeContext)
            operator fun invoke(
        contextId: String,
        buildViews: ViewComposeContext<SCV>.() -> Unit
    ): SelectableListComposeContext {
        return getOrCreateChildContext(contextId) { id, context, viewLifecycleOwner ->
            SelectableListComposeContext(
                id,
                context,
                viewLifecycleOwner,
                buildViews
            )
        }
    }
}

class SelectableListComposeContext(
    contextId: String,
    context: Context,
    viewLifecycleOwner: LifecycleOwner,
    buildViews: ViewComposeContext<SCV>.() -> Unit
) : ListComposeContext(contextId, context, viewLifecycleOwner, buildViews)