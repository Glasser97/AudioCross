package com.grayson.audiocross.domain.player

enum class PlaybackSpeed(val milliseconds: Long, val speed: Float) {
    HalfSpeed(500, 0.5F),
    ZeroEightSpeed(800, 0.8F),
    DefaultSpeed(1000, 1F),
    OneTwoSpeed(1200, 1.2F),
    OneAndHalfSpeed(1500, 1.5F),
    DoubleSpeed(2000, 2F)
}