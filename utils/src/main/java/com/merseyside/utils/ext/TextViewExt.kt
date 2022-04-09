package com.merseyside.utils.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

fun TextView.setTextSilently(text: String, textWatcher: TextWatcher) {
    removeTextChangedListener(textWatcher)
    setText(text)
    addTextChangedListener(textWatcher)
}

fun TextView.onTextChanged(callback: (String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {}

    }
    addTextChangedListener(textWatcher)
    return textWatcher
}