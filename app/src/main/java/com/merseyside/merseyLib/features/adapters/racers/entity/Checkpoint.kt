package com.merseyside.merseyLib.features.adapters.racers.entity

import com.merseyside.merseyLib.time.units.Millis

data class Checkpoint(
    val racer: RacerModel,
    val team: Team,
    val time: Millis,
    val gap: Millis
)
