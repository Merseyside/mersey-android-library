package com.merseyside.adapters.compose.view.text

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.view.base.addView
import com.merseyside.adapters.compose.style.ComposingStyle

open class ComposingText<Style : ComposingTextStyle>(
    id: String,
    override val composingStyle: Style
) : StyleableComposingView<Style>(id) {

    var text: String = ""

    companion object {
        context (ComposeContext) operator fun invoke(
            id: String,
            style: ComposingTextStyle.() -> Unit = {},
            init: Text.() -> Unit
        ): Text {
            return ComposingText(id, ComposingTextStyle(style))
                .apply(init)
                .addView()
        }
    }

    override fun getStringBuilder(): StringBuilder {
        return super.getStringBuilder().apply {
            appendLine()
            append("text: ").appendLine(text)
        }
    }
}

open class ComposingTextStyle : ComposingStyle() {
    @ColorRes
    var textColor: Int? = null

    @DimenRes
    var textSize: Int? = null
    var gravity: Int? = null
    var maxLines: Int? = null

    companion object {
        operator fun invoke(init: ComposingTextStyle.() -> Unit): ComposingTextStyle {
            return ComposingTextStyle().apply(init)
        }
    }

    override val tag: String = "TextStyle"
}

typealias Text = ComposingText<ComposingTextStyle>