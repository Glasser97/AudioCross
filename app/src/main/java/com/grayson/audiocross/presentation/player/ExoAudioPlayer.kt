package com.grayson.audiocross.presentation.player

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.domain.player.PlaybackSpeed
import com.grayson.audiocross.domain.player.PlayerState
import com.grayson.audiocross.domain.player.hasNext
import com.grayson.audiocross.domain.player.hasPrevious
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty


/**
 * This singleton Player represents the interface between the application and the media playback service running in the background
 * It expose the current state of the MediaSessionService as state flows so UI can update accordingly
 */
class ExoAudioPlayer(
    private val context: Context,
    private val mainDispatcher: CoroutineDispatcher
) : IAudioPlayer {

    // region constant

    companion object {
        private const val TAG = "ExoAudioPlayer"

        private const val POSITION_UPDATE_INTERNAL = 1000L
    }

    // endregion

    // region field

    private val _playerState = MutableStateFlow(PlayerState())
    private val _currentAudio = MutableStateFlow<TrackItem.Audio?>(null)
    private val queue = MutableStateFlow<List<TrackItem.Audio>>(emptyList())
    private val isPlaying = MutableStateFlow(false)
    private val timePosition = MutableStateFlow(0L)
    private val _playerSpeed = MutableStateFlow(PlaybackSpeed.DefaultSpeed)
    private val coroutineScope = CoroutineScope(mainDispatcher)

    override val playerState: StateFlow<PlayerState> = _playerState
    override val currentAudio: TrackItem.Audio? by _currentAudio
    override val playbackSpeed: PlaybackSpeed = _playerSpeed.value
    // region media controller

    private val _isReady = MutableStateFlow(false)

    private val sessionToken =
        SessionToken(context, ComponentName(context, AudioService::class.java))

    private var mediaController: MediaController? = null

    private val mediaControllerFuture =
        MediaController.Builder(context, sessionToken).buildAsync().apply {
            this.addListener({
                if (this.isDone) {
                    _isReady.update { true }
                    mediaController = this.get()
                    mediaController?.addListener(
                        object : Player.Listener {

                            override fun onIsPlayingChanged(isPlaying: Boolean) {
                                super.onIsPlayingChanged(isPlaying)
                                if (isPlaying != _playerState.value.isPlaying) {
                                    _playerState.update {
                                        it.copy(isPlaying = isPlaying)
                                    }
                                }
                            }

                            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                                super.onPlayWhenReadyChanged(playWhenReady, reason)

                            }

                            override fun onPlaybackStateChanged(playbackState: Int) {
                                super.onPlaybackStateChanged(playbackState)
                                when (playbackState) {
                                    Player.STATE_IDLE -> {
                                        Log.i(TAG, "onPlaybackStateChanged: STATE_IDLE")
                                    }

                                    Player.STATE_BUFFERING -> {
                                        Log.i(TAG, "onPlaybackStateChanged: STATE_BUFFERING")
                                    }

                                    Player.STATE_ENDED -> {
                                        Log.i(TAG, "onPlaybackStateChanged: STATE_ENDED")
                                    }

                                    Player.STATE_READY -> {
                                        Log.i(TAG, "onPlaybackStateChanged: STATE_READY")
                                    }
                                }
                            }

                            override fun onPositionDiscontinuity(
                                oldPosition: Player.PositionInfo,
                                newPosition: Player.PositionInfo,
                                reason: Int
                            ) {
                                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                                Log.d(TAG, "onPositionDiscontinuity: $oldPosition, $newPosition, $reason")
                                _playerState.update {
                                    it.copy(timeElapsed = newPosition.positionMs)
                                }
                            }

                            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                                super.onMediaItemTransition(mediaItem, reason)
                            }

                            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                                super.onTimelineChanged(timeline, reason)
                            }

                            override fun onEvents(player: Player, events: Player.Events) {
                                super.onEvents(player, events)
                            }

                            override fun onPlayerError(error: PlaybackException) {
                                super.onPlayerError(error)
                            }

                            override fun onPlayerErrorChanged(error: PlaybackException?) {
                                super.onPlayerErrorChanged(error)
                            }
                        }
                    )
                }
            }, MoreExecutors.directExecutor())
        }

    // endregion

    // endregion

    // region init

    init {
        coroutineScope.launch {
            combine(
                _currentAudio,
                queue,
                isPlaying,
                timePosition,
                _playerSpeed
            ) { currentAudio, queue, isPlaying, timeElapsed, playerSpeed ->
                Log.d(TAG, "combine: $currentAudio, $queue, $isPlaying, $timeElapsed, $playerSpeed")
                PlayerState(
                    currentAudio = currentAudio,
                    playQueue = queue,
                    isPlaying = isPlaying,
                    timeElapsed = timeElapsed,
                    playbackSpeed = playerSpeed
                )
            }.catch {
                it.printStackTrace()
            }.collect { state ->
                Log.d(TAG, "collect: $state")
                _playerState.update {
                    state
                }
            }
        }
    }

    // endregion

    // region private

    // endregion

    // region override function

    override fun addToQueue(audio: TrackItem.Audio) {
        queue.update {
            it + audio
        }
        mediaController?.addMediaItem(audio.toMediaItem())
    }

    override fun removeAllFromQueue() {
        queue.update {
            emptyList()
        }
        mediaController?.clearMediaItems()
    }

    override fun play() {
        mediaController?.play()
    }

    override fun play(audio: TrackItem.Audio) {
        play(listOf(audio))
    }

    override fun play(audios: List<TrackItem.Audio>) {
        val mediaItems = audios.map { it.toMediaItem() }
        mediaController?.setMediaItems(mediaItems)
        mediaController?.prepare()
        mediaController?.play()
    }

    override fun pause() {
        if (mediaController?.isPlaying == true) {
            mediaController?.pause()
        }
    }

    override fun stop() {
        if (mediaController?.isPlaying == true) {
            mediaController?.stop()
        }
    }

    override fun next() {
        if (playerState.value.hasNext()) {
            mediaController?.seekToNext()
        }
    }

    override fun previous() {
        if (playerState.value.hasPrevious()) {
            mediaController?.seekToPrevious()
        }
    }

    override fun advanceBy(duration: Long) {
        val currentPosition = mediaController?.currentPosition ?: 0
        if (currentPosition + duration < (mediaController?.duration ?: 0)) {
            mediaController?.seekTo(currentPosition + duration)
        }
    }

    override fun rewindBy(duration: Long) {
        val currentPosition = mediaController?.currentPosition ?: 0
        if (currentPosition - duration >= 0) {
            mediaController?.seekTo(currentPosition - duration)
        }
    }

    override fun changePlaybackSpeed(speed: PlaybackSpeed) {
        if (speed.speed != mediaController?.playbackParameters?.speed) {
            mediaController?.setPlaybackSpeed(speed.speed)
        }
    }

    override fun onSeekingStarted() {
        pause()
    }

    override fun onSeekingFinished(duration: Long) {
        val currentAudioDuration = _currentAudio.value?.duration ?: return
        duration.coerceIn(0, currentAudioDuration)
        mediaController?.seekTo(duration)
    }

    // endregion
}

fun TrackItem.Audio.toMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(this.hash)
        .setUri(this.streamUrl)
        .build()
}

// Used to enable property delegation
private operator fun <T> MutableStateFlow<T>.setValue(
    thisObj: Any?,
    property: KProperty<*>,
    value: T
) {
    this.value = value
}

//private operator fun <T> MutableStateFlow<T>.getValue(thisObj: Any?, property: KProperty<*>): T =
//    this.value

// endregion