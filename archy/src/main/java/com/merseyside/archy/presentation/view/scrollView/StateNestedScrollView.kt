package com.merseyside.archy.presentation.view.scrollView

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import com.merseyside.merseyLib.kotlin.logger.log

class StateNestedScrollView(
    context: Context,
    attrsSet: AttributeSet
) : NestedScrollView(context, attrsSet) {

    var isSaveScrollPosition = false

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val newState = SavedState(superState)

        if (isSaveScrollPosition) {
            newState.mScrollPosition = scrollY
        }

        return newState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state != null) {

            if (state is SavedState) {
                super.onRestoreInstanceState(state.superState)
                scrollY = state.mScrollPosition
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

        companion object {
            @Suppress("UNUSED")
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun newArray(size: Int): Array<SavedState?> =
                    arrayOfNulls(size)

                override fun createFromParcel(source: Parcel) = SavedState(source)
            }
        }
    }
}