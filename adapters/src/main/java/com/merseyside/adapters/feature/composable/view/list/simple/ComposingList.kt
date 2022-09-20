package com.merseyside.adapters.feature.composable.view.list.simple

import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.feature.composable.SCV
import com.merseyside.adapters.feature.composable.StyleableComposingView
import com.merseyside.adapters.feature.composable.dsl.context.ListComposerContext
import com.merseyside.adapters.feature.composable.dsl.context.ComposeContext
import com.merseyside.adapters.feature.composable.dsl.context.list
import com.merseyside.adapters.feature.style.ComposingStyle

open class ComposingList(
    id: String,
    val viewList: List<SCV> = emptyList()
) : StyleableComposingView<ComposingListStyle>(id) {

    var decorator: RecyclerView.ItemDecoration? = null
    override val composingStyle: ComposingListStyle = ComposingListStyle()

    override fun toString(): String {
        return "${getId()}: $viewList"
    }

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
                .also { list -> add(list) }
        }
    }
}

class ComposingListStyle : ComposingStyle() {

    companion object {
        operator fun invoke(init: ComposingStyle.() -> Unit): ComposingListStyle {
            return ComposingListStyle().apply(init)
        }
    }
}