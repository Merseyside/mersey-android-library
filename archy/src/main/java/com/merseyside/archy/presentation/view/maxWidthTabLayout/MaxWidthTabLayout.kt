package com.merseyside.archy.presentation.view.maxWidthTabLayout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout
import com.merseyside.archy.R

class MaxWidthTabLayout(context: Context, attrSet: AttributeSet): TabLayout(context, attrSet) {

    private var mMaxWidth = 0

    init {
        loadAttributes(attrSet)
    }

    private fun loadAttributes(
        attrs: AttributeSet?
    ) {
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxWidthTabLayout)
        mMaxWidth = arr.getLayoutDimension(R.styleable.MaxWidthTabLayout_maxWidth, mMaxWidth)

        arr.recycle()
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        var widthMeasureSpecMut = widthMeasureSpec
        if (mMaxWidth > 0) {
            widthMeasureSpecMut = MeasureSpec.makeMeasureSpec(
                mMaxWidth,
                MeasureSpec.AT_MOST
            )
        }

        super.onMeasure(widthMeasureSpecMut, heightMeasureSpec )
    }
}