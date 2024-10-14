package com.grayson.audiocross.presentation.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.Duration

@Composable
fun PlayerScreen(modifier: Modifier = Modifier) {

}

/**
 * Wrapper around all actions for the player controls.
 */
data class PlayerControlActions(
    val onPlayPress: () -> Unit,
    val onPausePress: () -> Unit,
    val onAdvanceBy: (Duration) -> Unit,
    val onRewindBy: (Duration) -> Unit,
    val onNext: () -> Unit,
    val onPrevious: () -> Unit,
    val onSeekingStarted: () -> Unit,
    val onSeekingFinished: (newElapsed: Duration) -> Unit,
)