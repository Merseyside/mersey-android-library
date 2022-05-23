package com.merseyside.utils.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.ImageRequest
import com.merseyside.merseyLib.kotlin.safeLet
import com.merseyside.utils.coil.CircleCropStroke
import com.merseyside.utils.coil.CircleCropTransformation
import com.merseyside.utils.ext.getDrawableResourceIdByName

@BindingAdapter("app:srcCompat")
fun setDrawableSrcCompat(view: ImageView, drawable: Drawable?) {
    view.setImageDrawable(drawable)
}

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
    "drawable",
    "imageUrl",
    "imagePlaceholder",
    "cropCircle",
    "strokeWidth",
    "strokeColorRes",
    "crossfade",
    requireAll = false
)
fun ImageView.setImageWithCoil(
    drawable: Drawable?,
    imageUrl: String?,
    placeholder: Any?,
    isCropCircle: Boolean = false,
    strokeWidth: Float? = null,
    @ColorRes strokeColor: Int? = null,
    isCrossfade: Boolean = false
) {
    val builder = build(isCrossfade, isCropCircle, strokeWidth, strokeColor)

    try {
        val data = firstNotNull(drawable, imageUrl)
        load(data) {
            builder()
            placeholder(placeholder)
        }
    } catch(e: NullPointerException) {
        loadPlaceHolder(placeholder) {
            builder()
        }
    }
}

private fun ImageView.loadPlaceHolder(
    placeholder: Any?,
    builder: ImageRequest.Builder.() -> Unit
) {
    load(placeholder) { builder() }
}

private fun build(
    crossfade: Boolean,
    cropCircle: Boolean,
    strokeWidth: Float?,
    @ColorRes strokeColor: Int?
): ImageRequest.Builder.() -> Unit {
    return {
        this.crossfade(crossfade)
        if (cropCircle) {
            val stroke = safeLet(strokeWidth, strokeColor) { width, color ->
                CircleCropStroke(width, color)
            }
            transformations(CircleCropTransformation(stroke))
        }
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


private fun <T> firstNotNull(vararg data: T): T {
    return data.toList().firstNotNull()
}

@Throws(NullPointerException::class)
private fun <T> Collection<T>.firstNotNull(): T {
    return find { it != null } ?: throw NullPointerException("No non-null items found!")
}

