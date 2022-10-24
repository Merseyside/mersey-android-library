package com.merseyside.merseyLib.features.adapters.movies.adapter.views

import com.merseyside.adapters.compose.dsl.context.ListComposerContext
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.view.list.simple.ComposingList
import com.merseyside.adapters.compose.style.ComposingStyle
import com.merseyside.adapters.compose.view.list.simple.ComposingListStyle
import com.merseyside.merseyLib.R

object MarginComposingList {

    private fun initWithDefaults(init: ComposingListStyle.() -> Unit): ComposingListStyle.() -> Unit = {
        margins = ComposingStyle.Margins(start = R.dimen.normal_spacing)
        init()
    }

    context (ComposeContext) operator fun invoke(
        id: String,
        style: ComposingListStyle.() -> Unit = {},
        initList: ComposingList.() -> Unit = {},
        contextInit: ListComposerContext.() -> Unit
    ): ComposingList {
        return ComposingList(id, initWithDefaults(style), initList, contextInit)
    }
}