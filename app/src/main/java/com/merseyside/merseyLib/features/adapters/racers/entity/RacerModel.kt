package com.merseyside.merseyLib.features.adapters.racers.entity

import kotlinx.serialization.Serializable

@Serializable
data class RacerModel(
    val name: Racer,
    val image: String
)