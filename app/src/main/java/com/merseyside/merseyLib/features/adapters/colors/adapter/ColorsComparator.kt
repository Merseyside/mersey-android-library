package com.merseyside.merseyLib.features.adapters.colors.adapter

import android.graphics.Color
import com.merseyside.adapters.feature.compare.Comparator
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.merseyLib.features.adapters.colors.model.ColorItemViewModel

class ColorsComparator(
    private var comparisonRule: ColorComparisonRule
) : Comparator<HexColor, ColorItemViewModel>() {
    override fun compare(model1: ColorItemViewModel, model2: ColorItemViewModel): Int {
        return when(comparisonRule) {
            ColorComparisonRule.ASC -> model1.getColor().compareTo(model2.getColor())
            ColorComparisonRule.DESC -> model2.getColor().compareTo(model1.getColor())
            ColorComparisonRule.RAINBOW -> rainbowComparison(model1.getColor(), model2.getColor())
        }
    }

    private fun rainbowComparison(color1: Int, color2: Int): Int {
        val hsv1 = FloatArray(3)
        val hsv2 = FloatArray(3)

        Color.colorToHSV(color1, hsv1)
        Color.colorToHSV(color2, hsv2)

        if ((hsv1[0].toInt()) == (hsv2[0].toInt())) {
            return hsv1[2].toInt().compareTo(hsv2[2].toInt())
        }

        return hsv1[0].toInt().compareTo(hsv2[0].toInt())
    }

    fun setCompareRule(rule: ColorComparisonRule) {
        this.comparisonRule = rule
        updateAsync()
    }

    enum class ColorComparisonRule {
        ASC, DESC, RAINBOW
    }
}