package com.merseyside.adapters.feature.composable.dsl.context

import com.merseyside.adapters.feature.composable.StyleableComposingView

object list {
    operator fun invoke(
        init: ListComposerContext.() -> Unit
    ): ListComposerContext {
        val context = ListComposerContext()
        context.init()
        return context
    }
}

class ListComposerContext: ScreenComposerContext<StyleableComposingView<*>>()