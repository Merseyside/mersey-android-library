package com.merseyside.merseyLib.presentation.view.animatedCheckButton

interface ICheckableView {

    interface OnCheckedListener {
        fun onChecked(isChecked: Boolean)
    }

    var listener: OnCheckedListener?

    fun setOnCheckedListener(listener: OnCheckedListener) {
        this.listener = listener
    }

    fun isChecked(): Boolean

    fun setChecked(isChecked: Boolean)

    fun setForceChecked(isChecked: Boolean)

    var isCheckable: Boolean
}