package com.merseyside.utils.binding

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.merseyside.utils.view.ext.addTextChangeListener
import com.merseyside.utils.view.ext.getColorFromAttr
import com.merseyside.utils.view.ext.setTextWithCursor

/**
 * Use it only for keeping cursor in right position
 */
@BindingAdapter("text")
fun setText(textView: TextView, text: String?) {
    if (textView is EditText) {
        textView.setTextWithCursor(text)
    } else {
        textView.text = text
    }
}

@BindingAdapter(value = ["textAttrChanged"]) // AttrChanged required postfix
fun setTextListener(textView: TextView, listener: InverseBindingListener?) {
    textView.addTextChangeListener { _, _, _, _, _, _, _ ->
        listener?.onChange()
        true
    }
}

@InverseBindingAdapter(attribute = "text")
fun getText(textView: TextView): String {
    return textView.text.toString()
}

@BindingAdapter("resTextColor")
fun setTextColor(view: TextView, @ColorRes colorRes: Int?) {
    if (colorRes != null) {
        view.setTextColor(ContextCompat.getColor(view.context, colorRes))
    }
}

@BindingAdapter("attrTextColor")
fun setCustomTextColor(view: TextView, @AttrRes attrId: Int?) {
    if (attrId != null) {
        view.setTextColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("spanned")
fun setSpannedText(view: TextView, charSequence: CharSequence?) {
    if (charSequence != null) {
        view.text = charSequence
    }
}

@BindingAdapter("count")
fun setCount(view: TextView, collection: Collection<*>?) {
    if (collection != null) {
        view.text = collection.size.toString()
    } else {
        view.text = "0"
    }
}

@BindingAdapter("drawableWidth", "drawableHeight", requireAll = true)
fun setTextViewDrawableSize(view: TextView, drawableWidth: Float, drawableHeight: Float) {
    with(view) {
        val drawables = compoundDrawablesRelative
        val scaledDrawables = drawables.map { drawable ->
            drawable?.let {
                drawable.apply {
                    setBounds(
                        0,
                        0,
                        drawableWidth.toInt(),
                        drawableHeight.toInt()
                    )
                }
            }
        }

        with(scaledDrawables) {
            setCompoundDrawables(get(0), get(1), get(2), get(3))
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("scrollable")
fun TextView.setTextScrollable(isScrollable: Boolean) {
    if (isScrollable) {
        setOnTouchListener { view, event ->
            if (view.isFocused) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return@setOnTouchListener false
        }
    }
    return
}