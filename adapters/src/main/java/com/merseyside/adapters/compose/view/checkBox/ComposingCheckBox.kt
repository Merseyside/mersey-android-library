package com.merseyside.adapters.compose.view.checkBox

import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.view.base.addView
import com.merseyside.adapters.compose.view.base.selectable.SelectableComposingStyle
import com.merseyside.adapters.compose.view.base.selectable.SelectableComposingView

open class ComposingCheckBox(
    id: String,
    override val composingStyle: ComposingCheckBoxStyle
): SelectableComposingView<ComposingCheckBoxStyle>(id) {

    companion object {
        context (ComposeContext) operator fun invoke(
            id: String,
            style: ComposingCheckBoxStyle.() -> Unit = {},
            init: ComposingCheckBox.() -> Unit
        ): ComposingCheckBox {
            return ComposingCheckBox(id, ComposingCheckBoxStyle(style))
                .apply(init)
                .addView()
        }
    }
}

class ComposingCheckBoxStyle: SelectableComposingStyle() {
    companion object {
        operator fun invoke(init: ComposingCheckBoxStyle.() -> Unit): ComposingCheckBoxStyle {
            return ComposingCheckBoxStyle().apply(init)
        }
    }
}

