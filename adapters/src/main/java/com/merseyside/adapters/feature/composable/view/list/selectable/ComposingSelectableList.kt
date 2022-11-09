package com.merseyside.adapters.feature.composable.view.list.selectable

import com.merseyside.adapters.feature.composable.SCV
import com.merseyside.adapters.feature.composable.dsl.context.ComposeContext
import com.merseyside.adapters.feature.composable.dsl.context.ListComposerContext
import com.merseyside.adapters.feature.composable.dsl.context.list
import com.merseyside.adapters.feature.composable.view.list.simple.ComposingList

class ComposingSelectableList(
    id: String,
    viewList: List<SCV> = emptyList()
): ComposingList(id, viewList) {

    companion object {
        context(ComposeContext) operator fun invoke(
            id: String,
            initList: ComposingSelectableList.() -> Unit = {},
            contextInit: ListComposerContext.() -> Unit
        ): ComposingList {
            val listContext = list(contextInit)
            val views = listContext.views

            return ComposingSelectableList(id, views)
                .apply(initList)
                .also { list -> add(list) }
        }
    }
}