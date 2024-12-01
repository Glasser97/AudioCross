package com.grayson.audiocross.presentation.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.domain.player.PlaybackSpeed
import com.grayson.audiocross.domain.player.PlayerState
import com.grayson.audiocross.domain.player.PlayingState
import com.grayson.audiocross.presentation.navigator.model.PlayerBarState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class PlayerViewModel : ViewModel() {

    // region constant

    companion object {
        private const val TAG = "PlayerViewModel"

        const val POSITION_UPDATE_INTERNAL = 1000L
    }

    // endregion

    // region field

    private val audioPlayer: IAudioPlayer by inject(IAudioPlayer::class.java)

    val playerUiState: StateFlow<PlayerState> = audioPlayer.playerState

    private val _playerBarState = MutableStateFlow<PlayerBarState?>(null)
    val playerBarState: StateFlow<PlayerBarState?> = _playerBarState

    private var job: Job? = null

    // endregion

    // region init

    init {
        viewModelScope.launch {
            audioPlayer.playerState.collect { playState ->
                if (playState.currentAudio != null
                    && playState.playingState != PlayingState.PAUSED
                ) {
                    startUpdateState()
                } else {
                    stopUpdateState()
                }
                _playerBarState.update {
                    PlayerBarState(
                        title = playState.currentAudio?.title ?: "",
                        workTitle = playState.currentAudio?.workTitle ?: "",
                        coverUrl = playState.currentAudio?.work?.coverUrl ?: "",
                        progress = playState.currentPosition.toFloat() / playState.duration,
                        playingState = playState.playingState
                    )
                }
            }
        }
    }

    // endregion

    // region public

    fun startUpdateState() {
        if (job != null) {
            return
        }
        job = viewModelScope.launch {
            while (this.isActive) {
                updateState()
                delay(POSITION_UPDATE_INTERNAL)
            }
        }
    }

    fun stopUpdateState() {
        job?.cancel()
        job = null
    }

    fun pauseOrPlay() {
        when (audioPlayer.playerState.value.playingState) {
            PlayingState.PLAYING -> audioPlayer.pause()
            PlayingState.PAUSED -> audioPlayer.play()
            PlayingState.BUFFERING -> Unit
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
        stopUpdateState()
    }

// endregion
}