package com.grayson.audiocross.domain.player

import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import kotlinx.coroutines.flow.StateFlow

data class PlayerState(
    val currentAudio: TrackItem.Audio? = null,
    val playQueue: List<TrackItem.Audio> = emptyList(),
    val playbackSpeed: PlaybackSpeed = PlaybackSpeed.DefaultSpeed,
    val playingState: PlayingState = PlayingState.PAUSED,
    val currentPosition: Long = 0L,
    val duration: Long = 0L
) {
    companion object {
        val Empty = PlayerState()
    }
}

enum class PlayingState {
    PLAYING, PAUSED, BUFFERING
}

fun PlayerState.hasPrevious(): Boolean {
    val index = playQueue.indexOf(currentAudio)
    if (index == -1) return false
    return index > 0
}

fun PlayerState.hasNext(): Boolean {
    val index = playQueue.indexOf(currentAudio)
    if (index == -1) return false
    return index < playQueue.size - 1
}

interface IAudioPlayer {
    /**
     * The current state of the player.
     */
    val playerState: StateFlow<PlayerState>

    /**
     * The Speed of which the player increments in milliseconds
     */
    fun addToQueue(audio: TrackItem.Audio)

    /**
     * Flushes the queue
     */
    fun removeAllFromQueue()

    /**
     * play the current audio
     */
    fun play()

    /**
     * play the specified audio
     */
    fun play(audio: TrackItem.Audio)

    /**
     * play the specified list of audios
     */
    fun play(audios: List<TrackItem.Audio>, index: Int = 0)

    /**
     * pause the current audio
     */
    fun pause()

    /**
     * stop the current audio
     */
    fun stop()

    /**
     * skip to the next audio, if available
     */
    fun next()

    /**
     * Plays the previous episode in the queue (if available). Or if an episode is currently
     * playing this will start the episode from the beginning
     */
    fun previous()

    /**
     * Advances a currently played episode by a given time interval specified in [duration].
     */
    fun advanceBy(duration: Long)

    /**
     * Rewinds a currently played episode by a given time interval specified in [duration].
     */
    fun rewindBy(duration: Long)

    /**
     * change playback speed
     */
    fun changePlaybackSpeed(speed: PlaybackSpeed)

    /**
     * Signal that user started seeking.
     */
    fun onSeekingStarted()

    /**
     * Seeks to a given time interval specified in [duration] in milliseconds.
     */
    fun onSeekingFinished(duration: Long)

    /**
     * update player state
     */
    fun updateState()
}