package com.merseyside.adapters.feature.compositeScreen.view.text

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import com.merseyside.adapters.feature.compositeScreen.StyleableComposingView
import com.merseyside.adapters.feature.compositeScreen.dsl.context.ComposeContext
import com.merseyside.adapters.feature.style.ComposingStyle

open class ComposingText internal constructor(id: String): StyleableComposingView<ComposingTextStyle>(id) {

    override val composingStyle: ComposingTextStyle = ComposingTextStyle()
    override var style: ComposingTextStyle.() -> Unit = {}
        set(value) {
            composingStyle.apply(value)
        }

    var text: String = ""

    override fun toString(): String {
        return "${getId()}: $text"
    }

    companion object {
        context (ComposeContext) operator fun invoke(
            id: String,
            init: ComposingText.() -> Unit
        ): ComposingText {
            return ComposingText(id).apply(init).also { text ->
                add(text)
            }
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
}