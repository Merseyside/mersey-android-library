package com.merseyside.adapters.compose.view.base.selectable

import com.merseyside.adapters.feature.selecting.callback.HasOnItemSelectedListener
import com.merseyside.adapters.feature.selecting.callback.OnItemSelectedListener
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.view.list.simple.ComposingListStyle

abstract class SelectableComposingView<Style : SelectableComposingStyle>(id: String)
    : StyleableComposingView<Style>(id), HasOnItemSelectedListener<CSV> {

    override val selectedListeners: MutableList<OnItemSelectedListener<CSV>> by lazy {
        ArrayList()
    }

    var selected: Boolean = false

//    override val composingStyle: SelectableComposingStyle = SelectableComposingStyle()



//    companion object {
//        context (ComposeContext) operator fun invoke(
//            id: String,
//            init: CSV.() -> Unit
//        ): CSV {
//            return SelectableComposingView(id).apply(init).addView()
//        }
//    }


}

open class SelectableComposingStyle: ComposingListStyle() {

    override val tag: String = "SelectableStyle"

}

typealias CSV = SelectableComposingView<*>