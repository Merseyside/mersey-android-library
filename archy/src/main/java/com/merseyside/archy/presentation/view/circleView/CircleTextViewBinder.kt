package com.merseyside.archy.presentation.view.circleView

import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.merseyside.utils.ext.getColorFromAttr

@BindingAdapter("circleText")
fun setCircleText(view: CircleTextView, value: String?) {
    value?.let {
        view.setText(value)
    } ?: view.setText("")
}

@BindingAdapter("circleAttrColor")
fun setCircleAttrColor(view: CircleTextView, @AttrRes attrRes: Int?) {
    if (attrRes != null) {
        view.setColor(view.context.getColorFromAttr(attrRes))
    }
}

@BindingAdapter("circleColor")
fun setCircleColor(view: CircleTextView, @ColorRes colorRes: Int?) {
    if (colorRes != null) {
        view.setColor(ContextCompat.getColor(view.context, colorRes))
    }
}

@BindingAdapter("circleAttrTextColor")
fun setCircleAttrTextColor(view: CircleTextView, @AttrRes attrRes: Int?) {
    if (attrRes != null) {
        view.setTextColor(view.context.getColorFromAttr(attrRes))
    }
}