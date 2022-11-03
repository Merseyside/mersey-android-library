package com.merseyside.adapters.compose.view.checkBox

import com.merseyside.adapters.compose.view.text.ComposingTextViewModel
import com.merseyside.adapters.feature.selecting.SelectState
import com.merseyside.adapters.feature.selecting.SelectableItem
import com.merseyside.merseyLib.kotlin.logger.ILogger

class ComposingCheckBoxViewModel(
    item: CheckBox,
    override val selectState: SelectState = SelectState(item.checked)
) : ComposingTextViewModel<CheckBox>(item), SelectableItem, ILogger {

    init {
        selectState.setOnSelectStateListener(object: SelectState.OnSelectStateListener {
            override fun onSelected(selected: Boolean) {}

            override fun onSelectable(selectable: Boolean) {}

        })
    }

    override val tag: String = "ComposingCheckBox"
}