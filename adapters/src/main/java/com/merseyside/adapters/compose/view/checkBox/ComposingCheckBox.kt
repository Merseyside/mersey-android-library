package com.merseyside.adapters.compose.view.checkBox

import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.view.base.addView
import com.merseyside.adapters.compose.view.text.ComposingText
import com.merseyside.adapters.compose.view.text.ComposingTextStyle

open class ComposingCheckBox<Style : ComposingCheckBoxStyle>(
    id: String,
    override val composingStyle: Style
): ComposingText<Style>(id, composingStyle) {

    var checked: Boolean = false

    @Suppress("UNCHECKED_CAST")
    override fun getSuitableDelegate(): ViewDelegateAdapter<out ComposingCheckBox<Style>, Style, *> {
        return ComposingCheckBoxDelegate() as ViewDelegateAdapter<out ComposingCheckBox<Style>, Style, *>
    }

    companion object {
        context (ComposeContext) operator fun invoke(
            id: String,
            style: ComposingCheckBoxStyle.() -> Unit = {},
            init: CheckBox.() -> Unit
        ): CheckBox {
            return CheckBox(id, ComposingCheckBoxStyle(style))
                .apply(init)
                .addView()
        }
    }
}

open class ComposingCheckBoxStyle: ComposingTextStyle() {
    companion object {
        operator fun invoke(init: ComposingCheckBoxStyle.() -> Unit): ComposingCheckBoxStyle {
            return ComposingCheckBoxStyle().apply(init)
        }
    }
}

typealias CheckBox = ComposingCheckBox<ComposingCheckBoxStyle>