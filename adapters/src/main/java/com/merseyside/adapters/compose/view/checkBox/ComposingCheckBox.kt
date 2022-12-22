package com.merseyside.adapters.compose.view.checkBox

import android.content.Context
import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.view.text.ComposingText
import com.merseyside.adapters.compose.view.text.ComposingTextStyle
import com.merseyside.adapters.compose.viewProvider.ViewProviderContext
import com.merseyside.adapters.compose.viewProvider.addView

open class ComposingCheckBox<Style : ComposingCheckBoxStyle>(
    id: String,
    override val composingStyle: Style
) : ComposingText<Style>(id, composingStyle) {

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
            return CheckBox(id, ComposingCheckBoxStyle(context, style))
                .apply(init)
                .addView()
        }
    }
}

open class ComposingCheckBoxStyle(context: Context) : ComposingTextStyle(context) {
    companion object {
        operator fun invoke(
            context: Context,
            init: ComposingCheckBoxStyle.() -> Unit
        ): ComposingCheckBoxStyle {
            return ComposingCheckBoxStyle(context).apply(init)
        }
    }
}

typealias CheckBox = ComposingCheckBox<ComposingCheckBoxStyle>