package com.merseyside.utils.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.merseyside.utils.ext.getDrawableResourceIdByName

@BindingAdapter("app:drawableName")
fun ImageView.loadDrawableByName(name: String?) {
    if (name != null) {
        val drawableRes = context.getDrawableResourceIdByName(name)
        loadImageDrawable(drawableRes)
    }
}

@BindingAdapter("app:vectorDrawableName")
fun ImageView.loadVectorDrawableByName(name: String?) {
    if (name != null) {
        val drawableRes = context.getDrawableResourceIdByName(name)
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

@BindingAdapter(
    "imageUrl",
    "imagePlaceholder",
    "cropCircle",
    "crossfade",
    "roundedCorners",
    "radiusCorners",
    requireAll = false
)
fun ImageView.imageUrlPlaceholder(
    url: String?,
    placeholder: Any?,
    isCropCircle: Boolean = false,
    isCrossfade: Boolean = false,
    isRoundedCorners: Boolean = false,
    radiusCorners: Float = 0f
) {
    val builder = build(isCrossfade, isCropCircle, isRoundedCorners, radiusCorners)
    if (url.isNullOrEmpty()) {
        loadPlaceHolder(placeholder) {
            builder()
        }
    } else {
        load(url) {
            builder()
            this.placeholder(placeholder)
        }
    }
}

@BindingAdapter(
    "imageUri",
    "imagePlaceholder",
    "cropCircle",
    "crossfade",
    "roundedCorners",
    "radiusCorners",
    requireAll = false
)
fun ImageView.imageUriPlaceholder(
    uri: Uri?,
    placeholder: Any?,
    isCropCircle: Boolean = false,
    isCrossfade: Boolean = false,
    isRoundedCorners: Boolean = false,
    radiusCorners: Float = 0f
) {
    val builder = build(isCrossfade, isCropCircle, isRoundedCorners, radiusCorners)
    if (uri == null) {
        loadPlaceHolder(placeholder) {
            builder()
        }
    } else {
        load(uri) {
            builder()
            this.placeholder(placeholder)
        }
    }
}

@BindingAdapter(
    "drawable",
    "imagePlaceholder",
    "cropCircle",
    "crossfade",
    "roundedCorners",
    "radiusCorners",
    requireAll = false
)
fun ImageView.imageDrawablePlaceholder(
    drawable: Drawable?,
    placeholder: Any?,
    isCropCircle: Boolean = false,
    isCrossfade: Boolean = false,
    isRoundedCorners: Boolean = false,
    radiusCorners: Float = 0f
) {
    val builder = build(isCrossfade, isCropCircle, isRoundedCorners, radiusCorners)
    if (drawable == null) {
        loadPlaceHolder(placeholder, builder)
    } else {
        load(drawable) {
            builder()
            this.placeholder(placeholder)
        }
    }
}

private fun ImageView.loadPlaceHolder(
    placeholder: Any?,
    builder: ImageRequest.Builder.() -> Unit
) {
    when (placeholder) {
        null -> {}
        is Drawable -> load(placeholder) { builder() }
        is String -> load(placeholder) { builder() }
        is Int -> load(drawableResId = placeholder) { builder() }
        else -> throw IllegalArgumentException("Wrong placeholder type!")
    }
}

private fun build(
    crossfade: Boolean,
    cropCircle: Boolean,
    roundedCorners: Boolean,
    radiusCorners: Float
): ImageRequest.Builder.() -> Unit {
    return {
        if (crossfade) this.crossfade(crossfade)
        if (cropCircle) transformations(CircleCropTransformation())
        if (roundedCorners) transformations(RoundedCornersTransformation(radius = radiusCorners))
    }
}

private fun ImageRequest.Builder.placeholder(holder: Any?) = apply {
    when (holder) {
        null -> {}
        is Drawable -> placeholder(holder)
        is Int -> placeholder(holder)
        else -> throw IllegalArgumentException("Wrong placeholder type!")
    }
}