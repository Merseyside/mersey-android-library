package com.merseyside.adapters.compose.adapter

import com.merseyside.adapters.compose.delegate.ViewDelegatesManager
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.dsl.context.ScreenComposeContext
import com.merseyside.adapters.compose.model.ViewAdapterViewModel
import com.merseyside.adapters.compose.style.ComposingStyle
import com.merseyside.adapters.compose.view.base.ComposingView
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.view.viewGroup.ViewGroup
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.init.initAdapter
import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.interfaces.ext.addOrUpdateAsync
import com.merseyside.adapters.model.VM
import com.merseyside.merseyLib.kotlin.utils.safeLet

open class ViewCompositeAdapter<Parent, Model>(
    adapterConfig: AdapterConfig<Parent, Model>,
    override val delegatesManager: ViewDelegatesManager<Parent, Model> = ViewDelegatesManager()
) : CompositeAdapter<Parent, Model>(adapterConfig, delegatesManager)
        where Parent : SCV, Model : VM<Parent> {

    @Suppress("UNCHECKED_CAST")
    fun <View : SCV> findViewById(id: String): View? {
        models.forEach { model ->
            val view = model.item.log()
            val foundView = if (view is ViewGroup) view.findViewById(id)
            else if (view.getId() == id) view as View
            else null

            if (foundView != null) return foundView
        }

        return null
    }

    companion object {
        operator fun <Parent : SCV, Model> invoke(
            delegatesManager: ViewDelegatesManager<Parent, Model>,
            configure: AdapterConfig<Parent, Model>.() -> Unit
        ): ViewCompositeAdapter<Parent, Model>
                where Model : VM<Parent> {
            return initAdapter(::ViewCompositeAdapter, delegatesManager, configure)
        }
    }
}

typealias SimpleViewCompositeAdapter = ViewCompositeAdapter<SCV, ViewAdapterViewModel>