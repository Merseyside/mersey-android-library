package com.merseyside.adapters.compose.dsl.context

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.merseyside.adapters.compose.view.base.SCV

open class ViewGroupComposeContext<View : SCV>(
    contextId: String,
    context: Context,
    viewLifecycleOwner: LifecycleOwner,
    buildViews: ScreenComposeContext<View>.() -> Unit
) : ScreenComposeContext<View>(contextId, context, viewLifecycleOwner, buildViews)