package com.grayson.audiocross.domain.player

enum class PlaybackSpeed(val milliseconds: Long) {
    HalfSpeed(500),
    ZeroEightSpeed(800),
    DefaultSpeed(1000),
    OneTwoSpeed(1200),
    OneAndHalfSpeed(1500),
    DoubleSpeed(2000)
}