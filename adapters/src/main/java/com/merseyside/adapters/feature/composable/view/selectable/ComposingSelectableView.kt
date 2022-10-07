package com.merseyside.adapters.feature.composable.view.selectable

import com.merseyside.adapters.feature.composable.dsl.context.ComposeContext
import com.merseyside.adapters.feature.composable.view.base.StyleableComposingView
import com.merseyside.adapters.feature.composable.view.base.addView
import com.merseyside.adapters.feature.composable.view.list.simple.ComposingListStyle
import com.merseyside.adapters.feature.composable.view.text.ComposingText

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