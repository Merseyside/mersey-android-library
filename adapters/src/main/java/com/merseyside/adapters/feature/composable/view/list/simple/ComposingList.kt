package com.merseyside.adapters.feature.composable.view.list.simple

import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.feature.composable.view.base.StyleableComposingView
import com.merseyside.adapters.feature.composable.dsl.context.ListComposerContext
import com.merseyside.adapters.feature.composable.dsl.context.ComposeContext
import com.merseyside.adapters.feature.composable.dsl.context.list
import com.merseyside.adapters.feature.composable.view.base.addView
import com.merseyside.adapters.feature.style.ComposingStyle

open class ComposingList(
    id: String,
    open val viewList: List<SCV> = emptyList()
) : StyleableComposingView<ComposingListStyle>(id) {

    var decorator: RecyclerView.ItemDecoration? = null
    override val composingStyle: ComposingListStyle = ComposingListStyle()

    companion object {
        context(ComposeContext) operator fun invoke(
            id: String,
            initList: ComposingList.() -> Unit = {},
            contextInit: ListComposerContext.() -> Unit
        ): ComposingList {
            val listContext = list(contextInit)
            val views = listContext.views

            return ComposingList(id, views)
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
        operator fun invoke(init: ComposingStyle.() -> Unit): ComposingListStyle {
            return ComposingListStyle().apply(init)
        }
    }

    override val tag: String = "ListStyle"
}