package com.merseyside.adapters.compose.view.list.simple

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.dsl.context.ListComposeContext
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.dsl.context.list
import com.merseyside.adapters.compose.view.base.addView
import com.merseyside.adapters.compose.style.ComposingStyle
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.model.VM

open class ComposingList(
    id: String,
    val configure: ListConfig.() -> Unit,
    override val composingStyle: ComposingListStyle,
    open val viewList: List<SCV> = emptyList()
) : StyleableComposingView<ComposingListStyle>(id) {

    open val listConfig: ListConfig by lazy { ListConfig().apply(configure) }

    override fun getSuitableDelegate(): ViewDelegateAdapter<out StyleableComposingView<out ComposingListStyle>, out ComposingListStyle, *> {
        return ComposingListDelegate()
    }

    override fun getStringBuilder(): StringBuilder {
        return super.getStringBuilder().apply {
            appendLine()
            append("viewList: ").appendLine("$viewList")
        }
    }

    companion object {
        context(ComposeContext) operator fun invoke(
            id: String,
            configure: ListConfig.() -> Unit = {},
            style: ComposingListStyle.() -> Unit = {},
            contextInit: ListComposeContext.() -> Unit
        ): ComposingList {
            val listContext = list(contextInit)
            val views = listContext.views

            return ComposingList(id, configure, ComposingListStyle(style), views)
                .addView()
        }
    }
}

open class ListConfig: HasOnItemClickListener<SCV> {

    override val clickListeners: MutableList<OnItemClickListener<SCV>> = ArrayList()

    var adapterConfig: AdapterConfig<SCV, VM<SCV>>.() -> Unit = {}

    var decorator: RecyclerView.ItemDecoration? = null
    var layoutManager: LayoutManager? = null

}

open class ComposingListStyle : ComposingStyle() {

    companion object {
        operator fun invoke(init: ComposingListStyle.() -> Unit): ComposingListStyle {
            return ComposingListStyle().apply(init)
        }
    }

    override val tag: String = "ListStyle"
}