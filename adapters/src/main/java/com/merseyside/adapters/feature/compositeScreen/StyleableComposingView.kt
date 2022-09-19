package com.merseyside.adapters.feature.compositeScreen

import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.feature.style.ComposingStyle
import com.merseyside.adapters.feature.style.StyleableItem
import com.merseyside.merseyLib.kotlin.contract.Identifiable

abstract class ComposingView(private val id: String) : Identifiable<String>,
    HasOnItemClickListener<ComposingView> {
    override fun getId(): String {
        return id
    }

    override var clickListeners: MutableList<OnItemClickListener<ComposingView>> = ArrayList()
}

abstract class StyleableComposingView<Style : ComposingStyle> (
    id: String
) : ComposingView(id), StyleableItem<Style> {

    override var style: Style.() -> Unit = {}
        set(value) {
            composingStyle.apply(value)
        }
}

typealias SCV = StyleableComposingView<*>