package com.merseyside.archy.presentation.view.validationInputView

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("validText")
fun setValidationText(view: ValidationInputView, text: String) {
    view.text = text
}

@InverseBindingAdapter(attribute = "validText")
fun getWorkTime(view: ValidationInputView): String {
    return view.text
}

@BindingAdapter(value = ["validTextAttrChanged"])
fun setValidationTextChanged(
    view: ValidationInputView,
    listener: InverseBindingListener
) {
    view.setOnTextChangedListener {
        listener.onChange()
    }
}

@BindingAdapter("validValidator")
fun setValidator(view: ValidationInputView, validator: (String) -> Boolean) {
    view.validator = { text ->
        if (validator(text)) ValidationState.OK
        else ValidationState.ERROR
    }
}

@BindingAdapter("validFormatter")
fun setFormatter(view: ValidationInputView, formatter: (String) -> String) {
    view.formatter = formatter
}