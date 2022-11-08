package com.merseyside.adapters.compose.dsl.context

import com.merseyside.adapters.compose.view.base.SCV

object compose {
    operator fun invoke(init: ComposeContext.() -> Unit): ComposeContext {
        val context = ComposeContext()
        context.init()
        return context
    }
}

open class ScreenComposeContext<View: SCV> {
    private val mutViews: MutableList<View> = ArrayList()
    val views: List<View> = mutViews

    open fun add(view: View) {
        mutViews.add(view)
    }

    open fun add(views: List<View>) {
        mutViews.addAll(views)
    }
}

typealias ComposeContext = ScreenComposeContext<SCV>