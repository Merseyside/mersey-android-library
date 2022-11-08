package com.merseyside.adapters.compose.dsl.context

import com.merseyside.adapters.compose.view.base.SCV

object selectableList {
    operator fun invoke(
        init: SelectableListComposeContext.() -> Unit
    ): SelectableListComposeContext {
        val context = SelectableListComposeContext()
        context.init()
        return context
    }
}

class SelectableListComposeContext: ScreenComposeContext<SCV>()