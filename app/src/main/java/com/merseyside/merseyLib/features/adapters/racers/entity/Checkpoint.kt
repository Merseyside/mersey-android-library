package com.merseyside.merseyLib.features.adapters.racers.entity

import com.merseyside.utils.time.Millis


data class Checkpoint(
    val racer: RacerModel,
    val team: Team,
    val time: Millis,
    val gap: Millis
)
