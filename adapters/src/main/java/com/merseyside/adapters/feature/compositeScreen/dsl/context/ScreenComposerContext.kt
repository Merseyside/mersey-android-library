package com.merseyside.adapters.feature.compositeScreen.dsl.context

import com.merseyside.adapters.feature.compositeScreen.SCV

object compose {
    operator fun invoke(init: ComposeContext.() -> Unit): ComposeContext  {
        val context = ComposeContext()
        context.init()
        return context
    }
}

open class ScreenComposerContext<View: SCV> {
    private val mutViews: MutableList<View> = ArrayList()
    val views: List<View> = mutViews

    open fun add(view: View) {
        mutViews.add(view)
    }

    open fun add(views: List<View>) {
        mutViews.addAll(views)
    }
}

typealias ComposeContext = ScreenComposerContext<SCV>