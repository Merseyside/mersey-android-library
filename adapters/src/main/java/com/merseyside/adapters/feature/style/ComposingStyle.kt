package com.merseyside.adapters.feature.style

import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import com.merseyside.adapters.feature.composable.view.base.ComposingView
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.utils.getClassName

abstract class ComposingStyle : ILogger {
    var width: Int? = null
    var height: Int? = null

    var margins: Margins? = null

    @ColorRes var backgroundColor: Int? = null

    open var onClick: (ComposingView) -> Unit = {}
    var clickable: Boolean = true

    class Margins(
        @DimenRes val top: Int? = null,
        @DimenRes val bottom: Int? = null,
        @DimenRes val start: Int? = null,
        @DimenRes val end: Int? = null
    ) {
        constructor(@DimenRes margin: Int): this(
            margin, margin, margin, margin
        )

        constructor(@DimenRes horizontal: Int, @DimenRes vertical: Int): this(
            horizontal, vertical, horizontal, vertical
        )

        override fun toString(): String {
            val builder = StringBuilder()
            builder.apply {
                appendLine("*** Margins ***")
                //append("top: ").appendLine(t)
            }

            return builder.toString()
        }
    }

    @CallSuper
    open fun getStringBuilder(): StringBuilder {
        val builder = StringBuilder()
        builder.apply {
            append("View style: ").appendLine(getClassName())
            appendLine("*** Composite style ***").appendLine()
            append("width = ").appendLine(width)
            append("height = ").appendLine(height)
            appendLine(margins)
            append("backroundColor = ").appendLine(backgroundColor)
        }

        return builder
    }

    final override fun toString(): String {
        return getStringBuilder().toString()
    }
}