package com.merseyside.adapters.compose.view.list.selectable

import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.feature.selecting.callback.HasOnItemSelectedListener
import com.merseyside.adapters.feature.selecting.callback.OnItemSelectedListener
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.dsl.context.SelectableListComposeContext
import com.merseyside.adapters.compose.dsl.context.selectableList
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.view.base.addView
import com.merseyside.adapters.compose.view.list.simple.ComposingList
import com.merseyside.adapters.compose.view.list.simple.ComposingListStyle
import com.merseyside.adapters.compose.view.list.simple.ListConfig
import com.merseyside.adapters.feature.selecting.SelectableMode

@Suppress("UNCHECKED_CAST")
class ComposingSelectableList(
    id: String,
    configure: SelectableListConfig.() -> Unit,
    override val composingStyle: ComposingSelectableListStyle,
    override val viewList: List<SCV> = emptyList()
) : ComposingList(id, configure as ListConfig.() -> Unit, composingStyle, viewList) {

    override val listConfig: SelectableListConfig by lazy { SelectableListConfig().apply(configure) }

    override fun getSuitableDelegate():
            ViewDelegateAdapter<out StyleableComposingView<out ComposingListStyle>, out ComposingListStyle, *> {
        return ComposingSelectableListDelegate()
    }

    companion object {
        context(ComposeContext) operator fun invoke(
            id: String,
            style: ComposingSelectableListStyle.() -> Unit = {},
            configure: SelectableListConfig.() -> Unit = {},
            contextInit: SelectableListComposeContext.() -> Unit
        ): ComposingSelectableList {
            val listContext = selectableList(contextInit)
            val views = listContext.views

            return ComposingSelectableList(
                id,
                configure,
                ComposingSelectableListStyle(style),
                views
            ).addView()
        }
    }
}

class SelectableListConfig: ListConfig(), HasOnItemSelectedListener<SCV> {
    var selectableMode: SelectableMode = SelectableMode.SINGLE
    var isSelectEnabled: Boolean = true
    var isAllowToCancelSelection: Boolean = false

    override val selectedListeners: MutableList<OnItemSelectedListener<SCV>> = ArrayList()
}

open class ComposingSelectableListStyle : ComposingListStyle() {

    companion object {
        operator fun invoke(init: ComposingSelectableListStyle.() -> Unit): ComposingSelectableListStyle {
            return ComposingSelectableListStyle().apply(init)
        }
    }

    override val tag: String = "SelectableListStyle"
}