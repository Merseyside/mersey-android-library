package com.merseyside.archy.presentation.view.localeViews

import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

/**
 * LocaleTextView bindings
 */
@BindingAdapter("app:textData")
fun setText(textView: LocaleTextView, localeData: LocaleData?) {
    textView.setText(localeData)
}

@BindingAdapter("app:text")
fun setText(textView: LocaleTextView, @StringRes id: Int?) {
    textView.setText(id)
}

/**
 * LocaleEditText bindings
 */

@BindingAdapter("app:textData")
fun setText(editText: LocaleEditText, localeData: LocaleData?) {
    editText.setText(localeData)
}

@BindingAdapter("app:text")
fun setText(editText: LocaleEditText, @StringRes id: Int?) {
    editText.setText(id)
}

@BindingAdapter("app:hintData")
fun setHint(editText: LocaleEditText, localeData: LocaleData?) {
    editText.setHint(localeData)
}

@BindingAdapter("app:hint")
fun setHint(editText: LocaleEditText, @StringRes id: Int?) {
    editText.setHint(id)
}

/**
 * LocaleTextInputLayout bindings
 */
@BindingAdapter("app:hintData")
fun setHint(localeTextInputLayout: LocaleTextInputLayout, localeData: LocaleData?) {
    localeTextInputLayout.setHint(localeData)
}

@BindingAdapter("app:hint")
fun setHint(localeTextInputLayout: LocaleTextInputLayout, @StringRes id: Int?) {
    localeTextInputLayout.setHint(id)
}

/**
 * LocaleEditText bindings
 */

@BindingAdapter("app:textData")
fun setText(editText: LocaleTextInputEditText, localeData: LocaleData?) {
    editText.setText(localeData)
}

@BindingAdapter("app:text")
fun setText(editText: LocaleTextInputEditText, @StringRes id: Int?) {
    editText.setText(id)
}

@BindingAdapter("app:hintData")
fun setHint(editText: LocaleTextInputEditText, localeData: LocaleData?) {
    editText.setHint(localeData)
}

@BindingAdapter("app:hint")
fun setHint(editText: LocaleTextInputEditText, @StringRes id: Int?) {
    editText.setHint(id)
}

/**
 * LocaleButton bindings
 */

@BindingAdapter("app:textData")
fun setText(button: LocaleButton, localeData: LocaleData?) {
    button.setText(localeData)
}

@BindingAdapter("app:text")
fun setText(button: LocaleButton, @StringRes id: Int?) {
    button.setText(id)
}

/**
 * LocaleSwitch bindings
 */

@BindingAdapter("app:textData")
fun setText(switch: LocaleSwitch, localeData: LocaleData?) {
    switch.setText(localeData)
}

@BindingAdapter("app:text")
fun setText(switch: LocaleSwitch, @StringRes id: Int?) {
    switch.setText(id)
}
