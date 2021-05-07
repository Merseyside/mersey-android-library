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
import com.merseyside.utils.ext.log

open class MerseyRecyclerView(context: Context, attrSet: AttributeSet? = null) : RecyclerView(context, attrSet) {
    private var mMaxHeight = 0
    private var isSaveScrollPosition = true

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

                newState.mScrollPosition = scrollPosition.log("position = ")
            }
        }

        return newState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state != null) {

            if (state is SavedState) {
                super.onRestoreInstanceState(state.superState)

                val mScrollPosition = state.mScrollPosition
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
        var mScrollPosition = 0

        constructor(`in`: Parcel) : super(`in`) {
            mScrollPosition = `in`.readInt()
        }

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(mScrollPosition)
        }

    }

}