package com.merseyside.adapters.compose.delegate

import android.content.Context
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.adapters.compose.view.base.StyleableComposingView
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.style.ComposingStyle
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.utils.safeLet
import com.merseyside.utils.view.ext.setMarginsRes

abstract class ViewDelegateAdapter<View : StyleableComposingView<Style>, Style : ComposingStyle, Model>
    : DelegateAdapter<View, SCV, Model>()
        where Model : AdapterParentViewModel<View, SCV> {

    @CallSuper
    open fun applyStyle(context: Context, viewDataBinding: ViewDataBinding, style: Style) {
        with(style) {
            val view = viewDataBinding.root
            view.updateLayoutParams {
                safeLet(style.width) { width = it }
                safeLet(style.height) { height = it }

                margins?.run {
                    setMarginsRes(context, start, top, end, bottom)
                }
            }

            safeLet(backgroundColor) { color ->
                view.setBackgroundColor(ContextCompat.getColor(context, color))
            }
        }
    }

    @InternalAdaptersApi
    override fun onModelCreated(model: Model) {
        super.onModelCreated(model)
        model.clickEvent.observe { item -> model.item.notifyOnClick(item) }
    }

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        super.onBindViewHolder(holder, model, position)
        applyStyle(holder.context, holder.binding, model.item.composingStyle)
    }
}