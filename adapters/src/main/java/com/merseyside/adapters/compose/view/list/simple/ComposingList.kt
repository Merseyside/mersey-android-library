package com.merseyside.adapters.compose.view.list.simple

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.dsl.context.*
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.view.viewGroup.ComposingViewGroup
import com.merseyside.adapters.compose.view.viewGroup.ComposingViewGroupStyle
import com.merseyside.adapters.compose.viewProvider.ViewProviderContext
import com.merseyside.adapters.compose.viewProvider.addView
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.model.VM

open class ComposingList(
    id: String,
    val configure: ListConfig.() -> Unit,
    composingStyle: ComposingListStyle,
    listComposeContext: ListComposeContext
) : ComposingViewGroup<ComposingListStyle>(id, composingStyle, listComposeContext) {

    open val listConfig: ListConfig by lazy { ListConfig().apply(configure) }

    override fun getSuitableDelegate(): ViewDelegateAdapter<out StyleableComposingView<out ComposingListStyle>, out ComposingListStyle, *> {
        return ComposingListDelegate()
    }

    companion object {
        context(ComposeContext) operator fun invoke(
            id: String,
            configure: ListConfig.() -> Unit = {},
            style: ComposingListStyle.() -> Unit = {},
            buildViews: ComposeContext.() -> Unit
        ): ComposingList {
            val listContext = listContext(id, buildViews)

            return ComposingList(id, configure, ComposingListStyle(context, style), listContext)
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

open class ComposingListStyle(context: Context) : ComposingViewGroupStyle(context) {

    @Orientation
    val orientation: Int = RecyclerView.VERTICAL

    companion object {
        operator fun invoke(context: Context, init: ComposingListStyle.() -> Unit): ComposingListStyle {
            return ComposingListStyle(context).apply(init)
        }
    }

    override val tag: String = "ListStyle"
}