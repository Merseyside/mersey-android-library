package com.merseyside.utils.attributes

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.merseyside.utils.R
import com.merseyside.utils.convertDpToPixel
import com.merseyside.utils.ext.*
import java.lang.RuntimeException

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
        defValue: Float,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getDimensionOrNull(nameSpace, resName) ?: defValue

    fun getDimensionOrNull(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getResourceId(nameSpace, resName)?.let {
        context.resources.getDimension(it)
    } ?: let {
        try {
            with(attrSet.getAttributeFloatValue(nameSpace.namespace, resName, NO_VALUE_FLOAT)) {
                if (this == NO_VALUE_FLOAT) null
                else this
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            parseDimensionToFloat(resName, nameSpace)?.let {
                convertDpToPixel(context, it).toFloat()
            }
        }
    }

    fun getDimensionPixelSize(
        defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getDimensionPixelSizeOrNull(nameSpace, resName) ?: defValue

    fun getDimensionPixelSizeOrNull(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getResourceId(nameSpace, resName)?.let {
        context.resources.getDimensionPixelSize(it)
    } ?: let {
        try {
            with(attrSet.getAttributeFloatValue(nameSpace.namespace, resName, NO_VALUE_FLOAT)) {
                if (this == NO_VALUE_FLOAT) null
                else this
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            parseDimensionToFloat(resName, nameSpace)?.let {
                convertDpToPixel(context, it).toFloat()
            }
        }
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
        @ColorInt defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getColorOrNull(nameSpace, resName) ?: defValue

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
        defValue: Drawable,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getDrawableOrNull(nameSpace, resName) ?: defValue

    fun getDrawableOrNull(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = getResourceId(nameSpace, resName)?.let {
        ContextCompat.getDrawable(context, it)
    }

    private fun parseDimensionToFloat(resName: String, nameSpace: Namespace): Float? {
        return attrSet.getAttributeValue(nameSpace.namespace, resName)?.run {
            replace("dip", "").replace("sp", "")
        }?.toFloatOrNull()
    }

    companion object {
        internal const val NO_VALUE = -999
        internal const val NO_VALUE_FLOAT = -999F
    }
}

enum class Namespace(
    val namespace: String
) {
    ANDROID("xmlns:android=\"http://schemas.android.com/apk/res/android"),
    DEFAULT("http://schemas.android.com/apk/res-auto")
}