package com.merseyside.utils.binding

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndZero
import com.merseyside.utils.ext.setColor
import com.merseyside.utils.view.ext.getActivity
import com.merseyside.utils.view.ext.getColorFromAttr
import kotlin.math.ceil

fun <Binding: ViewDataBinding> View.getBinding(
    @LayoutRes layoutRes: Int,
    parent: ViewGroup? = null,
    attachToParent: Boolean = false
): Binding {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return DataBindingUtil.inflate<Binding>(inflater, layoutRes, parent, attachToParent).apply {
        lifecycleOwner = getActivity()
    }
}

fun <Binding: ViewDataBinding> View.getDataBinding(
    @LayoutRes layoutRes: Int,
    variableId: Int,
    obj: Any,
    parent: ViewGroup? = null,
    attachToParent: Boolean = false
): Binding {
    return getBinding<Binding>(layoutRes, parent, attachToParent).apply {
        setVariable(variableId, obj)
    }
}

@BindingAdapter("isVisibleOrGone")
fun isVisibleOrGone(view: View, isVisible: Boolean?) {
    when(isVisible) {
        true -> view.visibility = View.VISIBLE
        else -> view.visibility = View.GONE
    }
}

@BindingAdapter("isVisibleOrGone")
fun isVisibleOrGone(view: View, obj: String?) {
    view.visibility = if (obj.isNotNullAndEmpty()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("isVisibleOrGone")
fun isVisibleOrGone(view: View, collection: Collection<*>?) {
    view.visibility = if (collection.isNotNullAndEmpty()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("isVisibleOrGone")
fun isVisibleOrGone(view: View, number: Number?) {
    view.visibility = if (number != null && number != 0) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("isVisibleOrGone")
fun isVisibleOrGone(view: View, obj: Any?) {
    view.visibility = if (obj != null) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("isVisible")
fun isVisible(view: View, isVisible: Boolean?) {
    when(isVisible) {
        true -> view.visibility = View.VISIBLE
        else -> view.visibility = View.INVISIBLE
    }
}

@BindingAdapter("isVisible")
fun isVisible(view: View, collection: Collection<*>?) {
    view.visibility = if (collection.isNotNullAndEmpty()) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("isVisible")
fun isVisible(view: View, number: Number?) {
    view.visibility = if (number != null && number != 0) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("isVisible")
fun isVisible(view: View, obj: Any?) {
    view.visibility = if (obj != null) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("backgroundDrawable")
fun setDrawableBackground(view: View, @DrawableRes res: Int?) {
    if (res != null) {
        view.background = ContextCompat.getDrawable(view.context, res)
    }
}

@BindingAdapter("attrBackgroundColor")
fun setViewGroupBackgroundColor(viewGroup: ViewGroup, @AttrRes attrId: Int?) {
    if (attrId != null) {
        viewGroup.setBackgroundColor(viewGroup.getColorFromAttr(attrId))
    }
}

@BindingAdapter("attrBackgroundColor")
fun setViewAttrBackgroundColor(view: View, @AttrRes attrId: Int?) {
    if (attrId != null) {
        view.setBackgroundColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("backgroundColor")
fun setViewBackgroundColor(view: View, color: Int?) {
    if (color.isNotNullAndZero()) {
        view.setBackgroundColor(color)
    }
}

@BindingAdapter("backgroundColorRes")
fun setViewBackgroundResColor(view: View, @ColorRes colorId: Int?) {
    if (colorId.isNotNullAndZero()) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, colorId))
    }
}

@BindingAdapter("attrTint")
fun setAttrTint(view: View, @AttrRes attrId: Int?) {
    if (attrId != null) {
        val drawable = view.background

        drawable.setColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("colorTint")
fun setTint(view: View, @ColorRes colorRes: Int?) {
    if (colorRes != null) {
        val drawable = view.background

        drawable.setColor(ContextCompat.getColor(view.context, colorRes))
    }
}

/**
 * Ensures that the touchable area of [view] equal [minTouchTarget] by expanding the touch area
 * of a view beyond its actual view bounds. This adapter can be used expand the touchable area of a
 * view when other options (adding padding, for example) may not be available.
 *
 * Usage:
 * <ImageView
 *     ...
 *     app:ensureMinTouchArea="@{@dimen/min_touch_target}"
 *
 * @param view The view whose touch area may be expanded
 * @param minTouchTarget The minimum touch area expressed dimen resource
 */
@BindingAdapter("ensureMinTouchArea")
fun addTouchDelegate(view: View, minTouchTarget: Float) {
    val parent = view.parent as View
    parent.post {
        val delegate = Rect()
        view.getHitRect(delegate)

        val metrics = view.context.resources.displayMetrics
        val height = ceil(delegate.height() / metrics.density)
        val width = ceil(delegate.width() / metrics.density)
        val minTarget = minTouchTarget / metrics.density
        var extraSpace = 0
        if (height < minTarget) {
            extraSpace = (minTarget.toInt() - height.toInt()) / 2
            delegate.apply {
                top -= extraSpace
                bottom += extraSpace
            }
        }

        if (width < minTarget) {
            extraSpace = (minTarget.toInt() - width.toInt()) / 2
            delegate.apply {
                left -= extraSpace
                right += extraSpace
            }
        }

        parent.touchDelegate = TouchDelegate(delegate, view)
    }
}

