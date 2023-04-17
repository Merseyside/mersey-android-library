package com.merseyside.utils.binding

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.merseyside.merseyLib.kotlin.extensions.isNotZero
import com.merseyside.merseyLib.kotlin.utils.firstNotNull
import com.merseyside.merseyLib.kotlin.utils.safeLet
import com.merseyside.utils.coil.CircleCropStroke
import com.merseyside.utils.coil.CircleCropTransformation
import com.merseyside.utils.ext.getDrawableResourceIdByName
import java.nio.ByteBuffer

@BindingAdapter("srcCompat")
fun setDrawableSrcCompat(view: ImageView, drawable: Drawable?) {
    view.setImageDrawable(drawable)
}

@BindingAdapter("drawableName")
fun ImageView.loadDrawableByName(name: String?) {
    if (name != null) {
        val drawableRes = context.getDrawableResourceIdByName(name)
        loadImageDrawable(drawableRes)
    }
}

@BindingAdapter("vectorDrawableName")
fun ImageView.loadVectorDrawableByName(name: String?) {
    if (name != null) {
        val drawableRes = context.getDrawableResourceIdByName(name)
        loadImageDrawable(drawableRes)
    }
}

@BindingAdapter("bitmap")
fun ImageView.loadImageBitmap(bitmap: Bitmap?) {
    if (bitmap != null) {
        setImageBitmap(bitmap)
    }
}

@BindingAdapter("drawableRes")
fun ImageView.loadImageDrawable(@DrawableRes drawableRes: Int?) {
    if (drawableRes != null) {
        setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
    }
}

@BindingAdapter("vectorDrawable")
fun ImageView.loadVectorDrawable(@DrawableRes resId: Int?) {
    if (resId != null) {
        setImageResource(resId)
    }
}

@BindingAdapter(
    "drawable",
    "imageUrl",
    "imageUri",
    "rawSvg",
    "placeholder",
    "crossfade",
    "roundedCorners",
    "radiusCorners",
    "bottomLeftRadius",
    "bottomRightRadius",
    "topLeftRadius",
    "topRightRadius",
    "cropCircle",
    "cropStrokeColor",
    "cropStrokeWidth",
    "cropImageSize",
    "cropBackgroundColor",
    "allowHardware",
    requireAll = false
)

fun setImageWithCoil(
    imageView: ImageView,
    drawable: Drawable?,
    imageUrl: String?,
    imageUri: Uri?,
    rawSvg: String?,
    placeholder: Any?,
    isCrossfade: Boolean = false,
    isRoundedCorners: Boolean = false,
    radiusCorners: Float = 0f,
    bottomLeftRadius: Float = 0f,
    bottomRightRadius: Float = 0f,
    topLeftRadius: Float = 0f,
    topRightRadius: Float = 0f,
    isCropCircle: Boolean = false,
    @ColorInt cropStrokeColor: Int? = null,
    cropStrokeWidth: Float? = null,
    cropImageSize: Float? = null,
    @ColorInt cropBackgroundColor: Int? = null,
    allowHardware: Boolean = true
) {
    val builder = build(
        isCrossfade,
        isRoundedCorners,
        radiusCorners,
        bottomLeftRadius,
        bottomRightRadius,
        topLeftRadius,
        topRightRadius,
        isCropCircle,
        cropStrokeWidth,
        cropStrokeColor,
        cropImageSize,
        cropBackgroundColor,
        allowHardware
    )

    with(imageView) {
        try {
            val data = if (rawSvg != null) ByteBuffer.wrap(rawSvg.toByteArray())
            else firstNotNull(drawable, imageUrl, imageUri)
            load(data) {
                //listener { request, result -> result.log("CoilResult") }
                builder()
                placeholder?.let { placeholder(getValidPlaceholder(it)) }
            }
        } catch (e: NullPointerException) {
            loadPlaceholder(placeholder) { builder() }
        }
    }
}

private fun ImageView.loadPlaceholder(
    placeholder: Any?,
    builder: ImageRequest.Builder.() -> Unit
) {
    placeholder?.let {
        load(getValidPlaceholder(it)) { builder() }
    }
}

private fun build(
    crossfade: Boolean,
    roundedCorners: Boolean,
    radiusCorners: Float,
    bottomLeftRadius: Float,
    bottomRightRadius: Float,
    topLeftRadius: Float,
    topRightRadius: Float,
    cropCircle: Boolean,
    strokeWidth: Float?,
    @ColorInt strokeColor: Int?,
    cropImageSize: Float?,
    @ColorInt cropBackgroundColor: Int?,
    allowHardware: Boolean
): ImageRequest.Builder.() -> Unit {
    return {
        this.crossfade(crossfade)
        allowHardware(allowHardware)
        if (cropCircle) {
            val stroke = safeLet(strokeWidth, strokeColor) { width, color ->
                CircleCropStroke(width, color)
            }

            transformations(
                CircleCropTransformation(
                    stroke,
                    cropImageSize,
                    cropBackgroundColor
                )
            )
        } else if (roundedCorners) {
            transformations(RoundedCornersTransformation(radius = radiusCorners))
        } else if (bottomLeftRadius.isNotZero() ||
            bottomRightRadius.isNotZero() || topLeftRadius.isNotZero() || topRightRadius.isNotZero()
        ) {
            transformations(
                RoundedCornersTransformation(
                    bottomLeft = bottomLeftRadius,
                    bottomRight = bottomRightRadius,
                    topLeft = topLeftRadius,
                    topRight = topRightRadius
                )
            )
        }
    }
}

private fun getValidPlaceholder(placeholder: Any): Drawable {
    return when (placeholder) {
        is Drawable -> placeholder
        is Int -> ColorDrawable(placeholder)
        else -> throw IllegalArgumentException("Only drawable and color placeholders supported!")
    }
}

