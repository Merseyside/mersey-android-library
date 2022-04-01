package com.merseyside.utils.ext

import android.text.TextWatcher
import android.widget.TextView

fun TextView.setTextSilently(text: String, textWatcher: TextWatcher) {
    removeTextChangedListener(textWatcher)
    setText(text)
    addTextChangedListener(textWatcher)
}