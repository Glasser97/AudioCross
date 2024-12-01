package com.grayson.audiocross.presentation.navigator.model

import com.grayson.audiocross.domain.player.PlayingState

data class PlayerBarState(
    val title: String = "",
    val workTitle: String = "",
    val coverUrl: String = "",
    val progress: Float = 0f,
    val playingState: PlayingState = PlayingState.PLAYING
)
