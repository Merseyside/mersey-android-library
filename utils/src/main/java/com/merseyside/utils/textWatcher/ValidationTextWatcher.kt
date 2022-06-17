package com.merseyside.utils.textWatcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.merseyside.utils.ext.setTextSilently

abstract class ValidationTextWatcher(private val editText: EditText) : TextWatcher {
    private var prevText: String = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        prevText = s.toString()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        val newText = s.toString()
        val validText = getValidValue(newText, prevText)
        if (prevText != validText) {
            editText.setTextSilently(validText, this)
            onTextChanged(validText)
        }
    }

    abstract fun getValidValue(newText: String, oldText: String): String
    abstract fun onTextChanged(validText: String)
}