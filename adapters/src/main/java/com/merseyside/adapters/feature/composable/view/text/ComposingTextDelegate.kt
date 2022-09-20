package com.merseyside.adapters.feature.composable.view.text

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.BR
import com.merseyside.adapters.R
import com.merseyside.adapters.databinding.ViewComposingTextBinding
import com.merseyside.adapters.feature.composable.delegate.ViewDelegateAdapter
import com.merseyside.merseyLib.kotlin.utils.safeLet
import com.merseyside.utils.view.ext.setTextSizePx

class ComposingTextDelegate : ViewDelegateAdapter<ComposingText, ComposingTextStyle, ComposingTextViewModel>() {
    override fun applyStyle(
        context: Context,
        viewDataBinding: ViewDataBinding,
        style: ComposingTextStyle
    ) {
        super.applyStyle(context, viewDataBinding, style)
        val textBinding = viewDataBinding as ViewComposingTextBinding
        with(textBinding.text) {
            safeLet(style.textColor) { color -> setTextColor(ContextCompat.getColor(context, color)) }
            safeLet(style.textSize) { textSize -> setTextSizePx(context.resources.getDimensionPixelSize(textSize)) }
            safeLet(style.gravity) { gravity -> setGravity(gravity) }
            safeLet(style.maxLines) { maxLines -> setMaxLines(maxLines) }
        }
    }

    override fun getLayoutIdForItem(viewType: Int) = R.layout.view_composing_text
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: ComposingText) = ComposingTextViewModel(item)
}