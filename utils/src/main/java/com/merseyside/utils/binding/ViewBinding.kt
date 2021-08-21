package com.merseyside.utils.binding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.merseyside.utils.ext.getActivity
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.merseyside.utils.ext.getColorFromAttr
import com.merseyside.utils.ext.isNotNullAndEmpty
import com.merseyside.utils.ext.isNotNullAndZero
import com.merseyside.utils.ext.setColor

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

@BindingAdapter("app:isVisibleOrGone")
fun isVisibleOrGone(view: View, isVisible: Boolean?) {
    when(isVisible) {
        true -> view.visibility = View.VISIBLE
        else -> view.visibility = View.GONE
    }
}

@BindingAdapter("app:isVisibleOrGone")
fun isVisibleOrGone(view: View, obj: Any?) {
    view.visibility = if (obj != null) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("app:isVisibleOrGone")
fun isVisibleOrGone(view: View, obj: String?) {
    view.visibility = if (obj.isNotNullAndEmpty()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("app:isVisibleOrGone")
fun isVisibleOrGone(view: View, collection: Collection<*>?) {
    view.visibility = if (collection.isNotNullAndEmpty()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("app:isVisible")
fun isVisible(view: View, isVisible: Boolean) {
    when(isVisible) {
        true -> view.visibility = View.VISIBLE
        false -> view.visibility = View.INVISIBLE
    }
}

@BindingAdapter("app:isVisible")
fun isVisible(view: View, obj: Any?) {
    view.visibility = if (obj != null) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("app:isVisible")
fun isVisible(view: View, collection: Collection<*>?) {
    view.visibility = if (collection.isNotNullAndEmpty()) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("app:backgroundDrawable")
fun setDrawableBackground(view: View, @DrawableRes res: Int?) {
    if (res != null) {
        view.background = ContextCompat.getDrawable(view.context, res)
    }
}

@BindingAdapter("app:attrBackgroundColor")
fun setViewGroupBackgroundColor(viewGroup: ViewGroup, @AttrRes attrId: Int?) {
    if (attrId != null) {
        viewGroup.setBackgroundColor(viewGroup.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:attrBackgroundColor")
fun setViewAttrBackgroundColor(view: View, @AttrRes attrId: Int?) {
    if (attrId != null) {
        view.setBackgroundColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:backgroundColor")
fun setViewBackgroundColor(view: View, color: Int?) {
    if (color.isNotNullAndZero()) {
        view.setBackgroundColor(color)
    }
}

@BindingAdapter("app:backgroundColorRes")
fun setViewBackgroundResColor(view: View, @ColorRes colorId: Int?) {
    if (colorId.isNotNullAndZero()) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, colorId))
    }
}

@BindingAdapter("app:attrTint")
fun setAttrTint(view: View, @AttrRes attrId: Int?) {
    if (attrId != null) {
        val drawable = view.background

        drawable.setColor(view.getColorFromAttr(attrId))
    }
}

@BindingAdapter("app:colorTint")
fun setTint(view: View, @ColorRes colorRes: Int?) {
    if (colorRes != null) {
        val drawable = view.background

        drawable.setColor(ContextCompat.getColor(view.context, colorRes))
    }
}

