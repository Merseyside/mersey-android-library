package com.merseyside.utils.coil

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.applyCanvas
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.min

/**
 * A [Transformation] that crops an image using a centered circle as the mask.
 *
 * If you're using Jetpack Compose, use `Modifier.clip(CircleShape)` instead of this transformation
 * as it's more efficient.
 */
class CircleCropTransformation(
    private val strokeConfig: CircleCropStroke? = null,
    @Px private val imageSize: Float? = null,
    @ColorInt private val background: Int? = null
) : Transformation {

    override val cacheKey: String = javaClass.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            background?.let { color = background }
        }

        val minSize = min(input.width, input.height)

        val outputImageSize = imageSize?.toInt() ?: minSize
        val outputBitmap = imageSize?.let {
            Bitmap.createScaledBitmap(input, outputImageSize, outputImageSize, true)
        } ?: input
        val radius = minSize / 2f
        val output = createBitmap(minSize, minSize, input.config ?: Bitmap.Config.ARGB_8888)
        output.applyCanvas {

            drawCircle(radius, radius, radius, paint)
            paint.alpha = 255
            if (imageSize == null || outputImageSize >= minSize) {
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            }

            drawBitmap(
                outputBitmap,
                radius - outputImageSize / 2f,
                radius - outputImageSize / 2f,
                paint
            )

            strokeConfig?.let { config ->
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    color = config.color
                    strokeWidth = config.widthPx
                }.let { drawCircle(radius, radius, radius - config.widthPx / 2f, it) }
            }
        }

        return output
    }

    override fun equals(other: Any?) = other is CircleCropTransformation
    override fun hashCode() = javaClass.hashCode()
}

data class CircleCropStroke(val widthPx: Float, val color: Int)