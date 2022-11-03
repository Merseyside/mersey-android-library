package com.merseyside.adapters.compose.dsl.context

import com.merseyside.adapters.compose.view.base.SCV

object selectableList {
    operator fun invoke(
        init: SelectableListComposerContext.() -> Unit
    ): SelectableListComposerContext {
        val context = SelectableListComposerContext()
        context.init()
        return context
    }
}

class SelectableListComposerContext: ScreenComposerContext<SCV>()