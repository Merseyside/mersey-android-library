package com.merseyside.utils.binding

import androidx.annotation.AttrRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.merseyside.utils.ext.getColorFromAttr
import com.merseyside.utils.ext.isNotNullAndEmpty

/**/

@BindingAdapter("app:attrCardBackgroundColor")
fun setCardViewBackgroundColor(cardView: CardView, @AttrRes attrId: Int?) {
    if (attrId != null) {
        cardView.setCardBackgroundColor(cardView.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.isErrorEnabled = errorMessage.isNotNullAndEmpty()
    view.error = errorMessage
}

