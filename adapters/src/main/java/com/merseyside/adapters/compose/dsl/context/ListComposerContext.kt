package com.merseyside.adapters.compose.dsl.context

import com.merseyside.adapters.compose.view.base.SCV

object list {
    operator fun invoke(
        init: ListComposerContext.() -> Unit
    ): ListComposerContext {
        val context = ListComposerContext()
        context.init()
        return context
    }
}

class ListComposerContext: ScreenComposerContext<SCV>()