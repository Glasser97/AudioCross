package com.grayson.audiocross.presentation.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.domain.player.PlaybackSpeed
import com.grayson.audiocross.domain.player.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class PlayerViewModel : ViewModel() {

    // region costant

    companion object {
        private const val TAG = "PlayerViewModel"

        private const val POSITION_UPDATE_INTERNAL = 1000L
    }

    // endregion

    // region field

    private val audioPlayer: IAudioPlayer by inject(IAudioPlayer::class.java)

    val playerUiState: StateFlow<PlayerState> = audioPlayer.playerState

    private var job: Job? = null

    // endregion

    // region public

    fun startUpdateState() {
        job = viewModelScope.launch {
            while (this.isActive) {
                updateState()
                delay(1000)
            }
        }
    }

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

    fun updateState() {
        audioPlayer.updateState()
    }

    // endregion

    // region override

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        job = null
    }

    // endregion
}