package com.merseyside.merseyLib.features.adapters.colors.producer

import android.graphics.Color
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.util.*

class ColorProducer {

    private val colorsFlow = MutableSharedFlow<List<HexColor>>()
    fun getColorsSharedFlow(): SharedFlow<List<HexColor>> = colorsFlow

    suspend fun generateRandomColors(count: Int = 10) {
        val colors = (0 until count).map {

            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            HexColor(color)
        }

        colorsFlow.emit(colors)
    }
}