package com.merseyside.archy.presentation.view.animatedCheckButton

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("observableChecked")
fun setChecked(button: AnimatedCheckButton, isChecked: Boolean?) {
    if (isChecked != null && button.isChecked() != isChecked) {
        button.setForceChecked(isChecked)
    }
}

@BindingAdapter(value = ["app:observableCheckedAttrChanged"]) // AttrChanged required postfix
fun setCheckListener(button: AnimatedCheckButton, listener: InverseBindingListener?) {
    if (listener != null) {
        button.setOnCheckedListener(object: ICheckableView.OnCheckedListener {
            override fun onChecked(isChecked: Boolean) {
                listener.onChange()
            }
        })
    }
}

@InverseBindingAdapter(attribute = "app:observableChecked")
fun getChecked(button: AnimatedCheckButton): Boolean {
    return button.isChecked()
}


@BindingAdapter("observableCheckable")
fun setCheckable(button: AnimatedCheckButton, isCheckable: Boolean?) {
    if (isCheckable != null && button.isCheckable != isCheckable) {
        button.isCheckable = isCheckable
    }
}


/* AnimatedCheckImageButton */

@BindingAdapter("observableChecked")
fun setChecked(button: AnimatedCheckImageButton, isChecked: Boolean?) {
    if (isChecked != null && button.isChecked() != isChecked) {
        button.setForceChecked(isChecked)
    }
}

@BindingAdapter(value = ["app:observableCheckedAttrChanged"]) // AttrChanged required postfix
fun setCheckListener(button: AnimatedCheckImageButton, listener: InverseBindingListener?) {
    if (listener != null) {
        button.setOnCheckedListener(object: ICheckableView.OnCheckedListener {
            override fun onChecked(isChecked: Boolean) {
                listener.onChange()
            }
        })
    }
}

@InverseBindingAdapter(attribute = "app:observableChecked")
fun getChecked(button: AnimatedCheckImageButton): Boolean {
    return button.isChecked()
}


@BindingAdapter("observableCheckable")
fun setCheckable(button: AnimatedCheckImageButton, isCheckable: Boolean?) {
    if (isCheckable != null && button.isCheckable != isCheckable) {
        button.isCheckable = isCheckable
    }
}
