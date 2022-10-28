package com.merseyside.adapters.compose.view.list.selectable

import com.merseyside.adapters.feature.selecting.callback.HasOnItemSelectedListener
import com.merseyside.adapters.feature.selecting.callback.OnItemSelectedListener
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.dsl.context.SelectableListComposerContext
import com.merseyside.adapters.compose.dsl.context.selectableList
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.addView
import com.merseyside.adapters.compose.view.list.simple.ComposingList
import com.merseyside.adapters.compose.view.base.selectable.CSV
import com.merseyside.adapters.compose.view.list.simple.ComposingListStyle

class ComposingSelectableList(
    id: String,
    override val composingStyle: ComposingSelectableListStyle,
    override val viewList: List<CSV> = emptyList()
): ComposingList(id, composingStyle, viewList), HasOnItemSelectedListener<SCV> {

    override val selectedListeners: MutableList<OnItemSelectedListener<SCV>> = ArrayList()

    companion object {
        context(ComposeContext) operator fun invoke(
            id: String,
            style: ComposingSelectableListStyle.() -> Unit = {},
            initList: ComposingSelectableList.() -> Unit = {},
            contextInit: SelectableListComposerContext.() -> Unit
        ): ComposingList {
            val listContext = selectableList(contextInit)
            val views = listContext.views

            return ComposingSelectableList(id, ComposingSelectableListStyle(style), views)
                .apply(initList)
                .addView()
        }
    }
}

open class ComposingSelectableListStyle : ComposingListStyle() {

    companion object {
        operator fun invoke(init: ComposingSelectableListStyle.() -> Unit): ComposingSelectableListStyle {
            return ComposingSelectableListStyle().apply(init)
        }
    }

    override val tag: String = "ListStyle"
}