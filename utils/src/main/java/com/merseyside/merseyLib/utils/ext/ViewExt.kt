package com.merseyside.merseyLib.utils.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

@ColorInt
fun View.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    return this.context.getColorFromAttr(attrColor, typedValue, resolveRefs)
}

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

fun View.getActivity(): Activity {
    var context: Context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }

    throw IllegalStateException("View hasn't been bind to activity!")
}

/**
 * Base unregistrar for all view callbacks
 */
interface CallbackUnregistrar {
    fun removeCallback()
}

internal class TextChangeListenerUnregistrar(
    private val textView: TextView,
    private val textWatcher: TextWatcher
): CallbackUnregistrar {

    override fun removeCallback() {
        textView.removeTextChangedListener(textWatcher)
    }
}

fun TextView.addTextChangeListener(callback: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit): CallbackUnregistrar {
    val textWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback(s, start, before, count)
        }
    }

    this.addTextChangedListener(textWatcher)

    return TextChangeListenerUnregistrar(this, textWatcher)
}