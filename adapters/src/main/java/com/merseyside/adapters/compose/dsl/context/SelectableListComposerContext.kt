package com.merseyside.adapters.compose.dsl.context

import com.merseyside.adapters.compose.view.base.selectable.CSV

object selectableList {
    operator fun invoke(
        init: SelectableListComposerContext.() -> Unit
    ): SelectableListComposerContext {
        val context = SelectableListComposerContext()
        context.init()
        return context
    }
}

class SelectableListComposerContext: ScreenComposerContext<CSV>()