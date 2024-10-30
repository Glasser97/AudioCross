package com.grayson.audiocross.presentation.player.viewmodel

import androidx.lifecycle.ViewModel
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.domain.player.PlaybackSpeed
import com.grayson.audiocross.domain.player.PlayerState
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(private val audioPlayer: IAudioPlayer) : ViewModel() {

    // region field

    val playerUiState: StateFlow<PlayerState> = audioPlayer.playerState

    // endregion

    // region public

    fun play() {
        audioPlayer.play()
    }

    fun pause() {
        audioPlayer.pause()
    }

    fun next() {
        audioPlayer.next()
    }

    fun previous() {
        audioPlayer.previous()
    }

    fun advanceBy(duration: Long) {
        audioPlayer.advanceBy(duration)
    }

    fun rewindBy(duration: Long) {
        audioPlayer.rewindBy(duration)
    }

    fun changePlaybackSpeed(speed: PlaybackSpeed) {
        audioPlayer.changePlaybackSpeed(speed)
    }

    fun seekStarted() {
        audioPlayer.onSeekingStarted()
    }

    fun seekFinished(duration: Long) {
        audioPlayer.onSeekingFinished(duration)
    }

    fun addToQueue(audio: TrackItem.Audio) {
        audioPlayer.addToQueue(audio)
    }

    // endregion
}