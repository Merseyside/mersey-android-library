package com.merseyside.adapters.feature.compositeScreen.view.text

import androidx.databinding.Bindable
import com.merseyside.adapters.BR
import com.merseyside.adapters.feature.compositeScreen.ComposingView
import com.merseyside.adapters.feature.compositeScreen.SCV
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.utils.binding.getText

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