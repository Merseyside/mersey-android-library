package com.merseyside.utils.ext

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun ByteArray.toDrawable(context: Context): Drawable {
    return BitmapDrawable(context.resources, BitmapFactory.decodeByteArray(this, 0, size))
}