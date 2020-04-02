package com.merseyside.merseyLib.presentation.view

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputLayout
import com.merseyside.merseyLib.utils.ext.addTextChangeListener
import com.merseyside.merseyLib.utils.ext.getColorFromAttr
import com.merseyside.merseyLib.utils.ext.isNotNullAndEmpty
import com.merseyside.merseyLib.utils.ext.setTextWithCursor

@BindingAdapter("app:isVisibleOrGone")
fun isVisibleOrGone(view: View, isVisible: Boolean) {
    when(isVisible) {
        true -> view.visibility = VISIBLE
        false -> view.visibility = GONE
    }
}

@BindingAdapter("app:isVisibleOrGone")
fun isVisibleOrGone(view: View, obj: Any?) {
    view.visibility = if (obj != null) {
        VISIBLE
    } else {
        GONE
    }
}

@BindingAdapter("app:isVisibleOrGone")
fun isVisibleOrGone(view: View, obj: String?) {
    view.visibility = if (obj.isNotNullAndEmpty()) {
        VISIBLE
    } else {
        GONE
    }
}

@BindingAdapter("app:isVisibleOrGone")
fun isVisibleOrGone(view: View, collection: Collection<*>?) {
    view.visibility = if (collection.isNotNullAndEmpty()) {
        VISIBLE
    } else {
        GONE
    }
}

@BindingAdapter("app:isVisible")
fun isVisible(view: View, isVisible: Boolean) {
    when(isVisible) {
        true -> view.visibility = VISIBLE
        false -> view.visibility = INVISIBLE
    }
}

@BindingAdapter("app:isVisible")
fun isVisible(view: View, obj: Any?) {
    view.visibility = if (obj != null) {
        VISIBLE
    } else {
        INVISIBLE
    }
}

@BindingAdapter("app:isVisible")
fun isVisible(view: View, collection: Collection<*>?) {
    view.visibility = if (collection.isNotNullAndEmpty()) {
        VISIBLE
    } else {
        INVISIBLE
    }
}

@BindingAdapter("bind:backgroundDrawable")
fun setDrawableBackground(view: View, @DrawableRes res: Int?) {
    if (res != null) {
        view.background = ContextCompat.getDrawable(view.context, res)
    }
}

/**
 * It use only for setting cursor in right position
 */
@BindingAdapter("bind:text")
fun setText(textView: TextView, text: String?) {
    if (textView is EditText) {
        textView.setTextWithCursor(text)
    } else {
        textView.text = text
    }
}

@BindingAdapter(value = ["textAttrChanged"]) // AttrChanged required postfix
fun setTextListener(textView: TextView, listener: InverseBindingListener?) {
    textView.addTextChangeListener { _, _, _, _ -> listener?.onChange() }
}

@InverseBindingAdapter(attribute = "bind:text")
fun getText(textView: TextView): String? {
    return textView.text.toString()
}

/**/

@BindingAdapter("bind:vectorDrawable")
fun loadVectorDrawable(iv: ImageView, @DrawableRes resId: Int?) {
    if (resId != null) {
        iv.setImageResource(resId)
    }
}

@BindingAdapter("app:attrCardBackgroundColor")
fun setCardViewBackgroundColor(cardView: CardView, @AttrRes attrId: Int?) {
    if (attrId != null) {
        cardView.setCardBackgroundColor(cardView.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:attrBackgroundColor")
fun setViewGroupBackgroundColor(viewGroup: ViewGroup, @AttrRes attrId: Int?) {
    if (attrId != null) {
        viewGroup.setBackgroundColor(viewGroup.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:attrBackgroundColor")
fun setViewBackgroundColor(view: View, @AttrRes attrId: Int?) {
    if (attrId != null) {
        view.setBackgroundColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:attrTextColor")
fun setCustomTextColor(view: TextView, @AttrRes attrId: Int?) {
    if (attrId != null) {
        view.setTextColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:spanned")
fun setSpannedText(view: TextView, charSequence: CharSequence?) {
    if (charSequence != null) {
        view.text = charSequence
    }
}

@BindingAdapter("app:count")
fun setCount(view: TextView, collection: Collection<*>?) {
    if (collection != null) {
        view.text = collection.size.toString()
    } else {
        view.text = "0"
    }
}

@BindingAdapter("app:errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.isErrorEnabled = errorMessage.isNotNullAndEmpty()
    view.error = errorMessage
}

