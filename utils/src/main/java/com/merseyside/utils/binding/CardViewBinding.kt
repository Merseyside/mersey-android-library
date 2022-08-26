package com.merseyside.utils.binding

import androidx.annotation.AttrRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import com.merseyside.utils.view.ext.getColorFromAttr

@BindingAdapter("attrCardBackgroundColor")
fun setCardViewBackgroundColor(cardView: CardView, @AttrRes attrId: Int?) {
    if (attrId != null) {
        cardView.setCardBackgroundColor(cardView.getColorFromAttr(attrId))
    }
}

@BindingAdapter("errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.isErrorEnabled = errorMessage.isNotNullAndEmpty()
    view.error = errorMessage
}

