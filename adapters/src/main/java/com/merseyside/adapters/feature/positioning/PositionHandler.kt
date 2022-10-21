package com.merseyside.adapters.feature.positioning

import com.merseyside.adapters.utils.InternalAdaptersApi

interface PositionHandler {

    var position: Int

//    @Throws(IllegalStateException::class)
//    fun getPosition(): Int {
//        if (position == AdapterParentViewModel.NO_ITEM_POSITION) {
//            throw IllegalStateException("View has not initialized!")
//        }
//
//        return position
//    }

    @InternalAdaptersApi
    fun onPositionChanged(toPosition: Int) {
        if (position != toPosition) {
            onPositionChanged(fromPosition = position, toPosition = toPosition)
            position = toPosition
        }
    }

    fun onPositionChanged(fromPosition: Int, toPosition: Int)
}