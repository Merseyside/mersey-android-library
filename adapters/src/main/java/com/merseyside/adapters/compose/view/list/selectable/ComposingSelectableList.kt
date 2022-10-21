package com.merseyside.adapters.compose.view.list.selectable

import com.merseyside.adapters.callback.HasOnItemSelectedListener
import com.merseyside.adapters.callback.OnItemSelectedListener
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.dsl.context.SelectableListComposerContext
import com.merseyside.adapters.compose.dsl.context.selectableList
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.addView
import com.merseyside.adapters.compose.view.list.simple.ComposingList
import com.merseyside.adapters.compose.view.selectable.CSV

class ComposingSelectableList(
    id: String,
    override val viewList: List<CSV> = emptyList()
): ComposingList(id, viewList), HasOnItemSelectedListener<SCV> {

    override val selectedListeners: MutableList<OnItemSelectedListener<SCV>> = ArrayList()

    companion object {
        context(ComposeContext) operator fun invoke(
            id: String,
            initList: ComposingSelectableList.() -> Unit = {},
            contextInit: SelectableListComposerContext.() -> Unit
        ): ComposingList {
            val listContext = selectableList(contextInit)
            val views = listContext.views

            return ComposingSelectableList(id, views)
                .apply(initList)
                .addView()
        }
    }
}