package com.merseyside.adapters.feature.composable.view.text

import androidx.databinding.Bindable
import com.merseyside.adapters.BR
import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.model.AdapterParentViewModel

class ComposingTextViewModel(
    item: ComposingText
) : AdapterParentViewModel<ComposingText, SCV>(item) {

    override fun notifyUpdate() {
        super.notifyUpdate()
        notifyPropertyChanged(BR.text)
    }

    @Bindable
    fun getText(): String {
        return item.text
    }

}