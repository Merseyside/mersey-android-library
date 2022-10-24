package com.merseyside.adapters.compose.view.list.simple

import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.dsl.context.ListComposerContext
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.dsl.context.list
import com.merseyside.adapters.compose.view.base.addView
import com.merseyside.adapters.compose.style.ComposingStyle

open class ComposingList(
    id: String,
    override val composingStyle: ComposingListStyle,
    open val viewList: List<SCV> = emptyList()
) : StyleableComposingView<ComposingListStyle>(id) {

    var decorator: RecyclerView.ItemDecoration? = null

    companion object {
        context(ComposeContext) operator fun invoke(
            id: String,
            style: ComposingListStyle.() -> Unit = {},
            initList: ComposingList.() -> Unit = {},
            contextInit: ListComposerContext.() -> Unit
        ): ComposingList {
            val listContext = list(contextInit)
            val views = listContext.views

            return ComposingList(id, ComposingListStyle(style), views)
                .apply(initList)
                .addView()
        }
    }

    override fun getStringBuilder(): StringBuilder {
        return super.getStringBuilder().apply {
            appendLine()
            append("viewList: ").appendLine("$viewList")
        }
    }
}

open class ComposingListStyle : ComposingStyle() {

    companion object {
        operator fun invoke(init: ComposingListStyle.() -> Unit): ComposingListStyle {
            return ComposingListStyle().apply(init)
        }
    }

    override val tag: String = "ListStyle"
}