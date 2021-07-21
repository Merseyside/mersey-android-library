package com.merseyside.utils.attributes

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.merseyside.utils.R
import com.merseyside.utils.convertDpToPixel
import com.merseyside.utils.ext.*

class AttributeHelper(
    view: View,
    private val attrSet: AttributeSet,
    val defNamespace: Namespace = Namespace.DEFAULT
) {

    val context: Context = view.getActivity()

    fun getBool(
        defValue: Boolean,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeBooleanValue(nameSpace.namespace, resName, defValue)

    fun getString(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ): String {
        val attrId = getAttributeId(nameSpace, resName)
        return attrId?.let {
            context.getStringFromAttr(it)
        } ?: with(getResourceId(nameSpace, resName)) {
            this?.let {
                context.resources.getString(this)
            } ?: attrSet.getAttributeValue(nameSpace.namespace, resName)
        }
    }

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
    ) = getResourceId(nameSpace, resName)?.let {
        context.resources.getDimension(it)
    } ?: with(attrSet.getAttributeFloatValue(nameSpace.namespace, resName, -1F)) {
        if (this == -1F) null
        else this
    } ?: context.resources.getDimension(defValue)

    fun getDimensionPixelSize(
        @DimenRes defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ): Int =
        getDimensionPixelSizeOrNull(nameSpace, resName) ?: context.resources.getDimensionPixelSize(
            defValue
        )


    fun getDimensionPixelSizeOrNull(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getResourceId(nameSpace, resName)?.let {
        context.resources.getDimensionPixelSize(it)
    } ?: with(attrSet.getAttributeFloatValue(nameSpace.namespace, resName, -1F)) {
        if (this == -1F) null
        else convertDpToPixel(context, this)
    }

    fun getResourceId(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ): Int? {
        val possibleResId =
            attrSet.getAttributeResourceValue(nameSpace.namespace, resName, NO_VALUE)

        return if (possibleResId == NO_VALUE) {
            getAttributeId(nameSpace, resName)?.let {
                context.getResourceFromAttr(it)
            }
        } else {
            possibleResId
        }
    }

    @AttrRes
    private fun getAttributeId(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ): Int? {
        val value = attrSet.getAttributeValue(nameSpace.namespace, resName)

        return if (value.isNotNullAndEmpty() && value.startsWith("?")) {
            val attrRes = value.substring(1)
            if (attrRes.containsDigits()) {
                return attrRes.toInt()
            } else {
                null
            }
        } else {
            null
        }
    }

    @ColorInt
    fun getColor(
        @ColorRes defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getColorOrNull(nameSpace, resName) ?: ContextCompat.getColor(context, defValue)

    @ColorInt
    fun getColorOrNull(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getResourceId(nameSpace, resName)?.let {
        ContextCompat.getColor(context, it)
    } ?: with(attrSet.getAttributeIntValue(nameSpace.namespace, resName, NO_VALUE)) {
        if (this == NO_VALUE) null
        else this
    }

    fun getDrawable(
        @DrawableRes defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getDrawableOrNull(nameSpace, resName)
        ?: ContextCompat.getDrawable(context, defValue)
        ?: throw Resources.NotFoundException()

    fun getDrawableOrNull(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getResourceId(nameSpace, resName)?.let {
        ContextCompat.getDrawable(context, it)
    }

    companion object {
        internal const val NO_VALUE = -1
    }
}

enum class Namespace(
    val namespace: String
) {
    ANDROID("xmlns:android=\"http://schemas.android.com/apk/res/android"),
    DEFAULT("http://schemas.android.com/apk/res-auto")
}