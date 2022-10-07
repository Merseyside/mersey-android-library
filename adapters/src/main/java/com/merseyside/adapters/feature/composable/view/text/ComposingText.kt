package com.merseyside.adapters.feature.composable.view.text

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import com.merseyside.adapters.feature.composable.view.base.StyleableComposingView
import com.merseyside.adapters.feature.composable.dsl.context.ComposeContext
import com.merseyside.adapters.feature.composable.view.base.addView
import com.merseyside.adapters.feature.style.ComposingStyle

open class ComposingText(id: String): StyleableComposingView<ComposingTextStyle>(id) {

    override val composingStyle: ComposingTextStyle = ComposingTextStyle()

    var text: String = ""

    companion object {
        context (ComposeContext) operator fun invoke(
            id: String,
            init: ComposingText.() -> Unit
        ): ComposingText {
            return ComposingText(id).apply(init).addView()
        }
    }

    override fun getStringBuilder(): StringBuilder {
        return super.getStringBuilder().apply {
            appendLine()
            append("text: ").appendLine(text)
        }
    }
}

class ComposingTextStyle: ComposingStyle() {
    @ColorRes var textColor: Int? = null
    @DimenRes var textSize: Int? = null
    var gravity: Int? = null
    var maxLines: Int? = null

    companion object {
        operator fun invoke(init: ComposingTextStyle.() -> Unit): ComposingTextStyle {
            return ComposingTextStyle().apply(init)
        }
    }

    override val tag: String = "TextStyle"
}