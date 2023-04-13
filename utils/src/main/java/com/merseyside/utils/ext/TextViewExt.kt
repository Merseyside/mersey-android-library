package com.merseyside.utils.ext

import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import com.merseyside.utils.view.ext.CallbackUnregistrar
import com.merseyside.utils.view.ext.TextChangeListenerUnregistrar
import com.merseyside.utils.view.ext.getColorFromAttr
import com.merseyside.utils.ext.setTextWithCursor

fun TextView.setTextSilently(text: String, textWatcher: TextWatcher) {
    removeTextChangedListener(textWatcher)
    if (this is EditText) setTextWithCursor(text)
    else setText(text)
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

fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
    //create span builder and replaces current text with it
    text = SpannableStringBuilder.valueOf(text).apply {
        //search for all URL spans and replace all spans with our own clickable spans
        getSpans(0, length, URLSpan::class.java).forEach {
            //add new clickable span at the same position
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onClicked?.invoke(it.url)
                    }
                },
                getSpanStart(it),
                getSpanEnd(it),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            //remove old URLSpan
            removeSpan(it)
        }
    }
    //make sure movement method is set
    movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.addTextChangeListener(
    callback: (
        view: TextView,
        newValue: String?,
        oldValue: String?,
        length: Int,
        start: Int,
        before: Int,
        count: Int
    ) -> Boolean // return true if new value is valid and should be saved
): CallbackUnregistrar {
    val textWatcher = object : TextWatcher {
        private var oldValue: String? = null

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val newValue = s?.toString()

            if (oldValue != newValue) {
                if (callback(
                        this@addTextChangeListener,
                        newValue,
                        oldValue,
                        newValue?.length ?: 0,
                        start,
                        before,
                        count
                    )
                ) {
                    oldValue = newValue
                }
            }
        }
    }
    this.addTextChangedListener(textWatcher)
    return TextChangeListenerUnregistrar(this, textWatcher)
}

fun TextView.setTextColorAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
) {
    setTextColor(getColorFromAttr(attrColor, typedValue, resolveRefs))
}

fun TextView.setTextSizePx(value: Number) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, value.toFloat())
}