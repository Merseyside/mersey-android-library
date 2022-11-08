package com.merseyside.adapters.compose.dsl.context

import com.merseyside.adapters.compose.view.base.SCV

object list {
    operator fun invoke(
        init: ListComposeContext.() -> Unit
    ): ListComposeContext {
        val context = ListComposeContext()
        context.init()
        return context
    }
}

class ListComposeContext: ScreenComposeContext<SCV>()