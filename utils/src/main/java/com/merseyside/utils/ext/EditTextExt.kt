package com.merseyside.utils.ext

import android.widget.EditText

fun EditText.setTextWithCursor(text: String?) {
    if (this.text.toString() != text) {
        text?.let {
            setText(it)
            setSelection(it.length)
        }
    }
}

fun EditText.setTextWithCursor(text: CharSequence?) {
    setTextWithCursor(text.toString())
}