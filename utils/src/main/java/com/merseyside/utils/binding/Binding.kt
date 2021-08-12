package com.merseyside.utils.binding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <Binding: ViewDataBinding> View.getBinding(
    @LayoutRes layoutRes: Int,
    parent: ViewGroup? = null,
    attachToParent: Boolean = false
): Binding {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return DataBindingUtil.inflate(inflater, layoutRes, parent, attachToParent)
}