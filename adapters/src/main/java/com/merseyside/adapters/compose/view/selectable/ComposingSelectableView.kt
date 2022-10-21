package com.merseyside.adapters.compose.view.selectable

import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.view.base.addView
import com.merseyside.adapters.compose.view.list.simple.ComposingListStyle

open class ComposingSelectableView(id: String) : StyleableComposingView<ComposingSelectableStyle>(id) {

    override val composingStyle: ComposingSelectableStyle = ComposingSelectableStyle()

    var selected: Boolean = false

    companion object {
        context (ComposeContext) operator fun invoke(
            id: String,
            init: ComposingSelectableView.() -> Unit
        ): ComposingSelectableView {
            return ComposingSelectableView(id).apply(init).addView()
        }
    }
}

class ComposingSelectableStyle: ComposingListStyle() {

    override val tag: String = "SelectableStyle"

}

typealias CSV = ComposingSelectableView