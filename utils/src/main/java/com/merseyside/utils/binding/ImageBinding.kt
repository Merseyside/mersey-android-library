package com.merseyside.utils.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.merseyside.utils.ext.log
import com.merseyside.utils.getDrawableResourceIdByName

@BindingAdapter("app:drawableName")
fun ImageView.loadDrawableByName(name: String?) {
    if (name != null) {
        val drawableRes = getDrawableResourceIdByName(context, name)

        loadImageDrawable(drawableRes)
    }
}

@BindingAdapter("app:vectorDrawableName")
fun ImageView.loadVectorDrawableByName(name: String?) {
    if (name != null) {
        val drawableRes = getDrawableResourceIdByName(context, name)

        loadImageDrawable(drawableRes)
    }
}

@BindingAdapter("app:bitmap")
fun ImageView.loadImageBitmap(bitmap: Bitmap?) {
    if (bitmap != null) {
        setImageBitmap(bitmap)
    }
}

@BindingAdapter("app:drawableRes")
fun ImageView.loadImageDrawable(@DrawableRes drawableRes: Int?) {
    if (drawableRes != null) {
        setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
    }
}

@BindingAdapter("app:vectorDrawable")
fun ImageView.loadVectorDrawable(@DrawableRes resId: Int?) {
    if (resId != null) {
        setImageResource(resId)
    }
}

@BindingAdapter("imageUrl", "imagePlaceholder", requireAll = false)
fun ImageView.imageUrl(url: String?, @DrawableRes placeholderId: Int?) {
    load(url) {
        placeholder(placeholderId?.let {
            ContextCompat.getDrawable(context, it)
        })
    }
}

@BindingAdapter("imageUrl", "imagePlaceholder", "cropCircle", "crossfade", requireAll = false)
fun ImageView.imageUrlDrawablePlaceholder(
    url: String?,
    placeholder: Drawable?,
    isCropCircle: Boolean = false,
    isCrossfade: Boolean = false
) {
    if (url.isNullOrEmpty()) {
        load(placeholder)
    } else {
        load(url) {
            if (isCrossfade) crossfade(true)
            this.placeholder(placeholder)
            if (isCropCircle) transformations(CircleCropTransformation())
        }
    }
}