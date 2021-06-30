package com.merseyside.utils.attributes

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class AttributeHelper(
    private val context: Context,
    private val attrSet: AttributeSet,
    val defNamespace: Namespace = Namespace.DEFAULT
) {

    fun getBool(
        defValue: Boolean,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeBooleanValue(nameSpace.namespace, resName, defValue)

    fun getString(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeValue(nameSpace.namespace, resName)

    fun getInt(
        defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeIntValue(nameSpace.namespace, resName, defValue)

    fun getFloat(
        defValue: Float,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeFloatValue(nameSpace.namespace, resName, defValue)

    fun getDimension(
        @DimenRes defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ): Float {
        val id = getResourceId(nameSpace, resName)
        return try {
            context.resources.getDimension(id)
        } catch (e: Resources.NotFoundException) {
            context.resources.getDimension(defValue)
        }
    }

    fun getDimensionPixelSize(
        @DimenRes defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ): Int {
        val id = getResourceId(nameSpace, resName)
        return try {
            context.resources.getDimensionPixelSize(id)
        } catch (e: Resources.NotFoundException) {
            context.resources.getDimensionPixelSize(defValue)
        }
    }

    fun getDimensionPixelSizeOrNull(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ): Int? {
        val id = getResourceId(nameSpace, resName)
        return try {
            context.resources.getDimensionPixelSize(id)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun getResourceId(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeResourceValue(nameSpace.namespace, resName, 0)

    @ColorInt
    fun getColor(
        @ColorRes defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = try {
        val id = getResourceId(nameSpace, resName)
        ContextCompat.getColor(context, id)
    } catch (e: Resources.NotFoundException) {
        ContextCompat.getColor(context, defValue)
    }

    @ColorInt
    fun getColorOrNull(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = try {
        val id = getResourceId(nameSpace, resName)
        ContextCompat.getColor(context, id)
    } catch (e: Resources.NotFoundException) {
        null
    }

    fun getDrawable(
        @DrawableRes defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = try {
        val id = getResourceId(nameSpace, resName)
        ContextCompat.getDrawable(context, id)
            ?: throw Resources.NotFoundException("Passed resource not found")
    } catch (e: Resources.NotFoundException) {
        ContextCompat.getDrawable(context, defValue)
            ?: throw Resources.NotFoundException("Default resource not found")
    }

    fun getDrawableOrNull(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = try {
        val id = getResourceId(nameSpace, resName)
        ContextCompat.getDrawable(context, id)
    } catch (e: Resources.NotFoundException) {
        null
    }
}

enum class Namespace(
    val namespace: String
) {
    ANDROID("xmlns:android=\"http://schemas.android.com/apk/res/android"),
    DEFAULT("http://schemas.android.com/apk/res-auto")
}