package com.merseyside.adapters.feature.positioning

import com.merseyside.adapters.utils.InternalAdaptersApi

interface PositionHandler {

    var position: Int

    @InternalAdaptersApi
    fun onPositionChanged(toPosition: Int) {
        if (position != toPosition) {
            onPositionChanged(fromPosition = position, toPosition = toPosition)
            position = toPosition
        }
    }

    fun onPositionChanged(fromPosition: Int, toPosition: Int)
}