package com.merseyside.adapters.compose.view.base

import androidx.annotation.CallSuper
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.adapters.compose.style.ComposingStyle
import com.merseyside.adapters.compose.style.StyleableItem
import com.merseyside.merseyLib.kotlin.contract.Identifiable
import com.merseyside.utils.getClassName

abstract class ComposingView(private val id: String) : Identifiable<String>,
    HasOnItemClickListener<ComposingView> {
    override fun getId() = id

    override val clickListeners: MutableList<OnItemClickListener<ComposingView>> by lazy {
        ArrayList()
    }

    @CallSuper
    open fun getStringBuilder(): StringBuilder {
        val builder = StringBuilder()
        builder.apply {
            appendLine()
            append("View: ").appendLine(this@ComposingView.getClassName())
            append("id: ").appendLine(id)
        }

        return builder
    }

    final override fun toString(): String {
        return getStringBuilder().toString()
    }


}

abstract class StyleableComposingView<Style : ComposingStyle> (
    id: String
) : ComposingView(id), StyleableItem<Style> {

    abstract fun getSuitableDelegate(): ViewDelegateAdapter<out StyleableComposingView<out Style>, out Style, *>
}

context(ComposeContext)
fun <View : SCV> View.addView(): View {
    add(this)
    return this
}

typealias SCV = StyleableComposingView<*>