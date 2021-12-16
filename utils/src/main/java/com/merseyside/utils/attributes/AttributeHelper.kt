package com.merseyside.utils.attributes

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.merseyside.merseyLib.kotlin.extensions.containsDigits
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import com.merseyside.merseyLib.kotlin.extensions.replace
import com.merseyside.utils.convertDpToPixel
import com.merseyside.utils.ext.getActivity
import com.merseyside.utils.ext.getResourceFromAttr
import com.merseyside.utils.ext.getStringFromAttr

class AttributeHelper(
    private val context: Context,
    private val attrSet: AttributeSet,
    val defNamespace: Namespace = Namespace.DEFAULT
) {

    constructor(view: View, attrSet: AttributeSet, defNamespace: Namespace = Namespace.DEFAULT)
            : this(view.getActivity(), attrSet, defNamespace)

    fun getBool(
        resName: String,
        defValue: Boolean,
        namespace: Namespace = Namespace.DEFAULT

    ) = attrSet.getAttributeBooleanValue(namespace.namespace, resName, defValue)

    fun getString(
        resName: String,
        namespace: Namespace = Namespace.DEFAULT
    ): String {
        val attrId = getAttributeId(resName, namespace)
        return attrId?.let {
            context.getStringFromAttr(it)
        } ?: with(getResourceId(resName, namespace)) {
            this?.let {
                context.resources.getString(this)
            } ?: attrSet.getAttributeValue(namespace.namespace, resName)
        }
    }

    fun getInt(
        resName: String,
        defValue: Int,
        namespace: Namespace = Namespace.DEFAULT
    ) = attrSet.getAttributeIntValue(namespace.namespace, resName, defValue)

    fun getFloat(
        resName: String,
        defValue: Float,
        namespace: Namespace = Namespace.DEFAULT
    ) = attrSet.getAttributeFloatValue(namespace.namespace, resName, defValue)

    fun getDimension(
        resName: String,
        defValue: Float,
        namespace: Namespace = Namespace.DEFAULT,
    ) = getDimensionOrNull(resName, namespace) ?: defValue

    fun getDimensionOrNull(
        resName: String,
        namespace: Namespace = Namespace.DEFAULT
    ) = getResourceId(resName, namespace)?.let {
        context.resources.getDimension(it)
    } ?: let {
        try {
            with(attrSet.getAttributeFloatValue(namespace.namespace, resName, NO_VALUE_FLOAT)) {
                if (this == NO_VALUE_FLOAT) null
                else this
            }
        } catch (e: RuntimeException) {
            parseDimensionToFloat(resName, namespace)?.let {
                convertDpToPixel(context, it)
            }
        }
    }

    fun getDimensionPixelSize(
        resName: String,
        defValue: Int,
        namespace: Namespace = Namespace.DEFAULT
    ) = getDimensionPixelSizeOrNull(resName, namespace) ?: defValue

    fun getDimensionPixelSizeOrNull(
        resName: String,
        namespace: Namespace = Namespace.DEFAULT
    ) = getResourceId(resName, namespace)?.let {
        context.resources.getDimensionPixelSize(it)
    } ?: let {
        try {
            with(attrSet.getAttributeFloatValue(namespace.namespace, resName, NO_VALUE_FLOAT)) {
                if (this == NO_VALUE_FLOAT) null
                else convertDpToPixel(context, this).toInt()
            }
        } catch (e: RuntimeException) {
            parseDimensionToFloat(resName, namespace)?.let {
                convertDpToPixel(context, it).toInt()
            }
        }
    }

    fun getResourceId(
        resName: String,
        namespace: Namespace = Namespace.DEFAULT
    ): Int? {
        return try {
            val possibleResId =
                attrSet.getAttributeResourceValue(namespace.namespace, resName, NO_VALUE)

            if (possibleResId == NO_VALUE) {
                getAttributeId(resName, namespace)?.let {
                    context.getResourceFromAttr(it)
                }
            } else {
                possibleResId
            }
        } catch (e: NullPointerException) {
            null
        }
    }

    @AttrRes
    private fun getAttributeId(
        resName: String,
        namespace: Namespace = Namespace.DEFAULT,
    ): Int? {
        return try {
            val value = attrSet.getAttributeValue(namespace.namespace, resName)

            if (value.isNotNullAndEmpty() && value.startsWith("?")) {
                val attrRes = value.substring(1)
                if (attrRes.containsDigits()) {
                    return attrRes.toInt()
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: NullPointerException) {
            null
        }
    }

    @ColorInt
    fun getColor(
        resName: String,
        @ColorInt defValue: Int,
        namespace: Namespace = Namespace.DEFAULT
    ) = getColorOrNull(resName, namespace) ?: defValue

    @ColorInt
    fun getColorOrNull(
        resName: String,
        namespace: Namespace = Namespace.DEFAULT
    ) = getResourceId(resName, namespace)?.let {
        ContextCompat.getColor(context, it)
    } ?: with(attrSet.getAttributeIntValue(namespace.namespace, resName, NO_VALUE)) {
        if (this == NO_VALUE) null
        else this
    }

    fun getDrawable(
        resName: String,
        defValue: Drawable,
        namespace: Namespace = Namespace.DEFAULT
    ) = getDrawableOrNull(resName, namespace) ?: defValue

    fun getDrawableOrNull(
        resName: String,
        namespace: Namespace = Namespace.DEFAULT
    ) = getResourceId(resName, namespace)?.let {
        ContextCompat.getDrawable(context, it)
    }

    private fun parseDimensionToFloat(resName: String, namespace: Namespace): Float? {
        return try {
            attrSet.getAttributeValue(namespace.namespace, resName)?.run {
                replace("dip", "sp", newValue = "")
            }?.toFloatOrNull()
        } catch (e: NullPointerException) {
            null
        }
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