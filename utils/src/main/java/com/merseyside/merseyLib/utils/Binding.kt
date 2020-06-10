package com.merseyside.merseyLib.utils

import android.graphics.Bitmap
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputLayout
import com.merseyside.merseyLib.utils.ext.*

@BindingAdapter("app:isVisibleOrGone")
fun isVisibleOrGone(view: View, isVisible: Boolean?) {
    when(isVisible) {
        true -> view.visibility = VISIBLE
        else -> view.visibility = GONE
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

@BindingAdapter("app:backgroundDrawable")
fun setDrawableBackground(view: View, @DrawableRes res: Int?) {
    if (res != null) {
        view.background = ContextCompat.getDrawable(view.context, res)
    }
}

/**
 * It use only for setting cursor in right position
 */
@BindingAdapter("app:text")
fun setText(textView: TextView, text: String?) {
    if (textView is EditText) {
        textView.setTextWithCursor(text)
    } else {
        textView.text = text
    }
}

@BindingAdapter(value = ["app:textAttrChanged"]) // AttrChanged required postfix
fun setTextListener(textView: TextView, listener: InverseBindingListener?) {
    textView.addTextChangeListener { _, _, _, _, _ -> listener?.onChange() }
}

@InverseBindingAdapter(attribute = "app:text")
fun getText(textView: TextView): String? {
    return textView.text.toString()
}

/**/

@BindingAdapter("app:drawableName")
fun loadDrawableByName(iv: ImageView, name: String?) {
    if (name != null) {
        val drawableRes = getDrawableResourceIdByName(iv.context, name)

        loadImageDrawable(iv, drawableRes)
    }
}

@BindingAdapter("app:vectorDrawableName")
fun loadVectorDrawableByName(iv: ImageView, name: String?) {
    if (name != null) {
        val drawableRes = getDrawableResourceIdByName(iv.context, name)

        loadImageDrawable(iv, drawableRes)
    }
}

@BindingAdapter("app:bitmap")
fun loadImageBitmap(iv: ImageView, bitmap: Bitmap?) {
    if (bitmap != null) {
        iv.setImageBitmap(bitmap)
    }
}

@BindingAdapter("app:drawableRes")
fun loadImageDrawable(iv: ImageView, @DrawableRes drawableRes: Int?) {
    if (drawableRes != null) {
        iv.setImageDrawable(ContextCompat.getDrawable(iv.context, drawableRes))
    }
}

@BindingAdapter("app:vectorDrawable")
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
fun setViewAttrBackgroundColor(view: View, @AttrRes attrId: Int?) {
    if (attrId != null) {
        view.setBackgroundColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:backgroundColorRes")
fun setViewBackgroundColor(view: View, @ColorRes colorId: Int?) {
    if (colorId.isNotNullAndZero()) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, colorId!!))
    }
}

@BindingAdapter("app:resTextColor")
fun setTextColor(view: TextView, @ColorRes colorRes: Int?) {
    if (colorRes != null) {
        view.setTextColor(ContextCompat.getColor(view.context, colorRes))
    }
}

@BindingAdapter("app:attrTextColor")
fun setCustomTextColor(view: TextView, @AttrRes attrId: Int?) {
    if (attrId != null) {
        view.setTextColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:attrTint")
fun setAttrTint(view: View, @AttrRes attrId: Int?) {
    if (attrId != null) {
        val drawable = view.background

        drawable.setColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:colorTint")
fun setTint(view: View, @ColorRes colorRes: Int?) {
    if (colorRes != null) {
        val drawable = view.background

        drawable.setColor(ContextCompat.getColor(view.context, colorRes))
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

