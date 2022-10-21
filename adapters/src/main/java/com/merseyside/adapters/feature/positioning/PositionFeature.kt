package com.merseyside.adapters.feature.positioning

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.feature.Feature
import com.merseyside.adapters.model.VM

class PositionFeature<Parent, Model : VM<Parent>> :
    Feature<Parent, Model>() {
    override val featureKey: String = key

    companion object {
        const val key = "positionFeature"
    }
}

object Positioning {
    context (AdapterConfig<Parent, Model>)operator fun <Parent, Model : VM<Parent>>
            invoke(): PositionFeature<Parent, Model> {
        val feature = PositionFeature<Parent, Model>()
        install(feature)
        return feature
    }
}
