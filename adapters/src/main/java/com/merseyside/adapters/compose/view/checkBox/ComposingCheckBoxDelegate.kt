package com.merseyside.adapters.compose.view.checkBox

import com.merseyside.adapters.BR
import com.merseyside.adapters.R
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.text.ComposingTextDelegate

class ComposingCheckBoxDelegate: ComposingTextDelegate<CheckBox,
        ComposingCheckBoxStyle, ComposingCheckBoxViewModel>() {
    override fun getLayoutIdForItem(viewType: Int) = R.layout.view_composing_checkbox
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: CheckBox) = ComposingCheckBoxViewModel(item)

    override fun isResponsibleForItemClass(clazz: Class<out SCV>): Boolean {
        return clazz == ComposingCheckBox::class.java
    }
}