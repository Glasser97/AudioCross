package com.grayson.audiocross.domain.player

import kotlin.math.abs

enum class PlaybackSpeed(val milliseconds: Long, val speed: Float) {
    HalfSpeed(500, 0.5F),
    ZeroEightSpeed(800, 0.8F),
    DefaultSpeed(1000, 1F),
    OneTwoSpeed(1200, 1.2F),
    OneAndHalfSpeed(1500, 1.5F),
    DoubleSpeed(2000, 2F);

    companion object {

        private const val SPEED_OFFSET = 0.05F

        fun fromFSpeed(speed: Float?): PlaybackSpeed {
            return entries.find { abs(it.speed - (speed ?: 1F)) <= SPEED_OFFSET } ?: DefaultSpeed
        }
    }
}