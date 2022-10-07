package com.merseyside.adapters.feature.composable.dsl.context

import com.merseyside.adapters.feature.composable.view.selectable.ComposingSelectableView

object selectableList {
    operator fun invoke(
        init: SelectableListComposerContext.() -> Unit
    ): SelectableListComposerContext {
        val context = SelectableListComposerContext()
        context.init()
        return context
    }
}

class SelectableListComposerContext: ScreenComposerContext<ComposingSelectableView>()