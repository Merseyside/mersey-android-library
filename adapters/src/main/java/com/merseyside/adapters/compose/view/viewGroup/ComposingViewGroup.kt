package com.merseyside.adapters.compose.view.viewGroup

import android.content.Context
import com.merseyside.adapters.compose.dsl.context.ViewGroupComposeContext
import com.merseyside.adapters.compose.style.ComposingStyle
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.StyleableComposingView

abstract class ComposingViewGroup<Style : ComposingViewGroupStyle>(
    id: String,
    override val composingStyle: Style,
    internal val viewGroupComposeContext: ViewGroupComposeContext<SCV>
) : StyleableComposingView<Style>(id) {

    val viewList: List<SCV>
        get() = viewGroupComposeContext.views

    override fun getStringBuilder(): StringBuilder {
        return super.getStringBuilder().apply {
            appendLine()
            append("viewList: ").appendLine("$viewList")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <View : SCV> findViewById(id: String): View? {
        viewList.forEach { view ->
            val foundView = if (view is ViewGroup) view.findViewById(id)
            else if (view.getId() == id) view as View
            else null

            if (foundView != null) return foundView
        }

        return null
    }
}

typealias ViewGroup = ComposingViewGroup<*>

open class ComposingViewGroupStyle(context: Context) : ComposingStyle(context) {
    override val tag: String = "ViewGroupStyle"

}
