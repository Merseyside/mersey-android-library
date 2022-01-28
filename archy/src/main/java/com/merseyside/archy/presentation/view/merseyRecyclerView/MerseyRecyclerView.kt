package com.merseyside.archy.presentation.view.merseyRecyclerView

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.archy.R

open class MerseyRecyclerView(context: Context, attrSet: AttributeSet? = null)
    : RecyclerView(context, attrSet) {
    private var mMaxHeight = 0
    var isSaveScrollPosition = true

    init {
        loadAttributes(attrSet)
    }

    private fun loadAttributes(
        attrs: AttributeSet?
    ) {
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MerseyRecyclerView)
        mMaxHeight = arr.getLayoutDimension(R.styleable.MerseyRecyclerView_maxHeight, mMaxHeight)

        arr.recycle()
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        var heightMeasureSpecMut = heightMeasureSpec
        if (mMaxHeight > 0) {
            heightMeasureSpecMut = MeasureSpec.makeMeasureSpec(
                mMaxHeight,
                MeasureSpec.AT_MOST
            )
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpecMut)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val newState = SavedState(superState)

        if (isSaveScrollPosition) {
            
            if (layoutManager != null) {

                val scrollPosition = when (val lm = layoutManager) {
                    is LinearLayoutManager -> lm.findFirstVisibleItemPosition()
                    is GridLayoutManager -> lm.findFirstVisibleItemPosition()
                    else -> 0
                }

                newState.scrollPosition = scrollPosition
            }
        }

        return newState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state != null) {

            if (state is SavedState) {
                super.onRestoreInstanceState(state.superState)

                val mScrollPosition = state.scrollPosition
                val layoutManager = layoutManager
                if (layoutManager != null) {
                    val count = layoutManager.itemCount
                    if (mScrollPosition != NO_POSITION && mScrollPosition < count) {
                        layoutManager.scrollToPosition(mScrollPosition)
                    }
                }
            } else {
                super.onRestoreInstanceState(state)
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    internal class SavedState : BaseSavedState {
        var scrollPosition = 0

        constructor(parcel: Parcel) : super(parcel) {

            scrollPosition = parcel.readInt()
        }

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(scrollPosition)
        }

        companion object {
            @Suppress("UNUSED")
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel) = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> =
                    arrayOfNulls(size)
            }
        }

    }

}