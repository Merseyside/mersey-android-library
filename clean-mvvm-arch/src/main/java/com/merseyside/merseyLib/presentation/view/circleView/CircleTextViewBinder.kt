package com.merseyside.merseyLib.presentation.view.circleView

import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.merseyside.merseyLib.utils.ext.getColorFromAttr

@BindingAdapter("app:circleText")
fun setCircleText(view: CircleTextView, value: String?) {
    value?.let {
        view.setText(value)
    } ?: view.setText("")
}

@BindingAdapter("app:circleAttrColor")
fun setCircleAttrColor(view: CircleTextView, @AttrRes attrRes: Int?) {
    if (attrRes != null) {
        view.setColor(view.context.getColorFromAttr(attrRes))
    }
}

@BindingAdapter("app:circleColor")
fun setCircleColor(view: CircleTextView, @ColorRes colorRes: Int?) {
    if (colorRes != null) {
        view.setColor(ContextCompat.getColor(view.context, colorRes))
    }
}

@BindingAdapter("app:circleAttrTextColor")
fun setCircleAttrTextColor(view: CircleTextView, @AttrRes attrRes: Int?) {
    if (attrRes != null) {
        view.setTextColor(view.context.getColorFromAttr(attrRes))
    }
}