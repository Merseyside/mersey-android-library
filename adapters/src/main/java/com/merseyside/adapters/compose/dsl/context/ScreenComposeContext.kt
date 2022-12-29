package com.merseyside.adapters.compose.dsl.context

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.merseyside.adapters.compose.adapter.ViewCompositeAdapter
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.viewProvider.MutableComposeState
import com.merseyside.adapters.compose.viewProvider.ViewComposeContext
import com.merseyside.adapters.compose.viewProvider.ViewProviderContext
import com.merseyside.merseyLib.kotlin.contract.Identifiable
import com.merseyside.adapters.interfaces.ext.addOrUpdateAsync
import com.merseyside.adapters.model.VM

internal object compose {
    operator fun invoke(
        context: Context,
        viewLifecycleOwner: LifecycleOwner,
        buildViews: ComposeContext.() -> Unit
    ): ComposeContext {
        return ComposeContext(rootContextId, context, viewLifecycleOwner, buildViews)
    }

    private const val rootContextId = "root_context"
}

open class ScreenComposeContext<View : SCV>(
    private val contextId: String,
    context: Context,
    viewLifecycleOwner: LifecycleOwner,
    private val buildViews: ScreenComposeContext<View>.() -> Unit
) : ViewComposeContext<View>(context, viewLifecycleOwner), Identifiable<String> {

    internal lateinit var relativeAdapter: ViewCompositeAdapter<SCV, VM<SCV>>

    private val childContextList = HashMap<String, ComposeContext>()

    private val composeStates: MutableList<MutableComposeState<*>> = ArrayList()

    @Suppress("UNCHECKED_CAST")
    fun <Cont : ComposeContext> getOrCreateChildContext(
        contextId: String,
        init: (contextId: String, context: Context, viewLifecycleOwner: LifecycleOwner) -> Cont
    ): Cont {
        val existContext = childContextList[contextId] as? Cont
        return existContext ?: init(contextId, context, viewLifecycleOwner).also {
            childContextList[contextId] = it
        }
    }

    override fun build() {
        buildViews()
    }

    override fun onUpdated(views: List<View>) {
        relativeAdapter.addOrUpdateAsync(views)
    }

    internal fun addComposeState(state: MutableComposeState<*>) {
        composeStates.add(state)
        state.observe {
            "update".log()
            update()
        }
    }

    fun getComposeState(propertyName: String): MutableComposeState<*>? {
        return composeStates.find { it.propertyName == propertyName }
    }

    override fun getId() = contextId

    override val tag: String = "ScreenComposeContext"
}

typealias ComposeContext = ScreenComposeContext<SCV>