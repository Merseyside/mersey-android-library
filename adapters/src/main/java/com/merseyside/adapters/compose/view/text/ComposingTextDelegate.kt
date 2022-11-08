package com.merseyside.adapters.compose.view.text

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.BR
import com.merseyside.adapters.R
import com.merseyside.adapters.databinding.ViewComposingTextBinding
import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.merseyLib.kotlin.utils.safeLet
import com.merseyside.utils.view.ext.setTextSizePx

open class ComposingTextDelegate<View : ComposingText<Style>, Style : ComposingTextStyle, VM : ComposingTextViewModel<View>> :
    ViewDelegateAdapter<View, Style, VM>() {
    override fun applyStyle(
        context: Context,
        viewDataBinding: ViewDataBinding,
        style: Style
    ) {
        super.applyStyle(context, viewDataBinding, style)
        val text = viewDataBinding.root as TextView
        with(text) {
            safeLet(style.textColor) { color ->
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        color
                    )
                )
            }
            safeLet(style.textSize) { textSize ->
                setTextSizePx(
                    context.resources.getDimensionPixelSize(
                        textSize
                    )
                )
            }
            safeLet(style.gravity) { gravity -> setGravity(gravity) }
            safeLet(style.maxLines) { maxLines -> setMaxLines(maxLines) }
        }
    }

    override fun getLayoutIdForItem(viewType: Int) = R.layout.view_composing_text
    override fun getBindingVariable() = BR.model
    @Suppress("UNCHECKED_CAST")
    override fun createItemViewModel(item: View) = ComposingTextViewModel(item) as VM

    override fun isResponsibleForItemClass(clazz: Class<out SCV>): Boolean {
        return clazz == ComposingText::class.java
    }
}