package com.merseyside.merseyLib.features.adapters.colors.producer

import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.utils.randomColor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ColorProducer {

    private val colorsFlow = MutableSharedFlow<List<HexColor>>()
    fun getColorsSharedFlow(): SharedFlow<List<HexColor>> = colorsFlow

    suspend fun generateRandomColors(count: Int = 10) {
        val colors = (0 until count).map {
            HexColor(randomColor())
        }

        colorsFlow.emit(colors)
    }
}